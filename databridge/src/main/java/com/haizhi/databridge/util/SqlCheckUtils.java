package com.haizhi.databridge.util;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import org.apache.commons.math3.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.web.result.StatusCode;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.QueryExplainReq;
import com.haizhi.dataclient.datapi.dmc.DmcTableApi;

/**
 * @author WangChengYu
 * @description: sql合法性检查工具类
 * @date 2020-02-11 22:29
 */
public final class SqlCheckUtils {

	private SqlCheckUtils() {
	}


	/**
	 * @throws
	 * @description: sql合法性性检查
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-03-02 20:35
	 */
	public static Pair<Boolean, CheckMsg> check(List<String> fields, String sql,
												Boolean isRowFormula, Boolean onlyWhere) {
		//为isRowFormula参数设置默认值
		isRowFormula = isRowFormula == null ? true : isRowFormula;
		DmcTableApi client = SpringUtils.getBean(DmcTableApi.class);
		String plan = null;
		//仅验证查询条件的合法性
		String fieldJoinStr = "";
		if (!CollectionUtils.isEmpty(fields)) {
			if (fields.get(0).endsWith("`") && fields.get(0).startsWith("`")) {
				fieldJoinStr = String.format("%s", String.join(", ", fields));
			} else {
				fieldJoinStr = String.format("`%s`", String.join("`, `", fields));
			}
		}

		if (onlyWhere) {
			plan = client.explain(QueryExplainReq.builder()
				.sql(MessageFormat.format("SELECT * FROM VALUES ({0}) AS ({1}) WHERE {2}",
				String.join(", ", Collections.nCopies(fields.size(), "\"abc\"")),
				fieldJoinStr,
				sql)).build()).getPlan();
		} else {
			plan = client.explain(QueryExplainReq.builder().sql(MessageFormat.format("SELECT {0} FROM VALUES ({1}) AS ({2})",
				sql,
				String.join(", ", Collections.nCopies(fields.size(), "\"abc\"")),
				fieldJoinStr)).build()).getPlan();
		}
		//图表计算字段中，出现count，sum或窗口函数中出现聚合函数的情况下，
		// 因为没有拼接group by语句，会导致公式验证不通过，这里临时过滤掉这种错误
		if (!isRowFormula && plan.contains("AnalysisException:") && plan.contains("an aggregate function")) {
			return new Pair<>(true, null);
		}
		//先做基础校验
		for (SqlError err : SqlError.values()) {
			//sql未能通过检查
			if (!SqlError.check(plan, err)) {
				return new Pair<>(false, err);
			}
		}
		//特殊校验
		if (isRowFormula) {
			for (SpecailSqlError err : SpecailSqlError.values()) {
				//sql未能通过检查
				if (!SpecailSqlError.check(plan, err)) {
					return new Pair<>(false, err);
				}
			}
		}
		return new Pair<>(true, null);
	}


	public interface CheckMsg {

		String[] getKeyword();

		int getCode();
	}

	@Getter
	public enum SqlError implements CheckMsg {
		a(new String[]{"+- Window", "SinglePartition"}, StatusCode.NOT_SUPPORT_SINGLE_PARTITION.getValue(), "Aggregate("),
		b(new String[]{"cannot resolve", "columns"}, StatusCode.INVALID_FIELD.getValue(), ""),
		c(new String[]{"Unresolved attributes:"}, StatusCode.FORMULA_ERROR.getValue(), ""),
		d(new String[]{"argument is expected"}, StatusCode.INVALID_ARGUMENTS.getValue(), ""),
		e(new String[]{"Invalid number of arguments"}, StatusCode.INVALID_ARGUMENTS.getValue(), ""),
		f(new String[]{"requires window to be ordered"}, StatusCode.MISSING_ORDER_BY.getValue(), ""),
		g(new String[]{"aggregate function in the argument of another aggregate function"},
			StatusCode.INVALID_AGGREGATE_USAGE.getValue(), ""),
		h(new String[]{"ndefined function"}, StatusCode.INVALID_FUNCTION.getValue(), ""),
		i(new String[]{"Exception"}, StatusCode.FORMULA_ERROR.getValue(), "");

		private String[] keyword;
		private String exclude;
		private int code;

		SqlError(String[] keyword, int code, String exclude) {
			this.keyword = keyword;
			this.exclude = exclude;
			this.code = code;
		}

		public static boolean check(String plan, SqlError err) {
			boolean keywordCheck = true;
			for (String key : err.getKeyword()) {
				if (!plan.contains(key)) {
					keywordCheck = false;
					break;
				}
			}
			return !keywordCheck || (!StringUtils.isEmpty(err.getExclude()) && plan.contains(err.getExclude()));
		}

	}

	@Getter
	public enum SpecailSqlError implements CheckMsg {
		a(new String[]{"Aggregate ["}, StatusCode.NOT_SUPPORT_AGGREGATE.getValue(), ""),
		b(new String[]{"Aggregate true"}, StatusCode.NOT_SUPPORT_AGGREGATE.getValue(), ""),
		c(new String[]{"TungstenAggregate"}, StatusCode.NOT_SUPPORT_AGGREGATE.getValue(), ""),
		d(new String[]{"SortBasedAggregate"}, StatusCode.NOT_SUPPORT_AGGREGATE.getValue(), ""),
		e(new String[]{"HashAggregate"}, StatusCode.NOT_SUPPORT_AGGREGATE.getValue(), "");

		private String[] keyword;
		private String exclude;
		private int code;

		SpecailSqlError(String[] keyword, int code, String exclude) {
			this.keyword = keyword;
			this.exclude = exclude;
			this.code = code;
		}

		static boolean check(String plan, SpecailSqlError err) {
			for (String key : err.getKeyword()) {
				if (plan.contains(key) && (StringUtils.isEmpty(err.getExclude()) || !plan.contains(err.getExclude()))) {
					return false;
				}
			}
			return true;
		}
	}

	public static void handlerError(CheckMsg checkMsg) {
		int code = checkMsg.getCode();
		if (code == StatusCode.FIELD_NO_PERMISSION.getValue()) {
			throw new DatabridgeException(StatusCode.TB_ACCESS_RIGHT_ERROR, "Permission deny");
		} else if (code == StatusCode.FIELD_DUPLICATED.getValue()) {
			throw new DatabridgeException(StatusCode.FIELD_DUPLICATED, checkMsg.getKeyword()[0]);
		} else if (code == StatusCode.FIELD_RELY_ERROR.getValue()) {
			throw new DatabridgeException(StatusCode.FIELD_RELY_ERROR,
				String.format("%s is referenced by itself", checkMsg.getKeyword()[0]));
		} else if (code == StatusCode.FIELD_AGGR_ERROR.getValue()) {
			throw new DatabridgeException(StatusCode.FIELD_AGGR_ERROR, checkMsg.getKeyword()[0]);
		} else if (code == StatusCode.VFIELD_FORMAT_ERROR.getValue()) {
			throw new DatabridgeException(StatusCode.VFIELD_FORMAT_ERROR, "Formula is lack of '(' ");
		} else if (code == StatusCode.INVALID_FIELD.getValue()) {
			throw new DatabridgeException(StatusCode.INVALID_FIELD, "Invalid field");
		} else if (code == StatusCode.INVALID_FUNCTION.getValue()) {
			throw new DatabridgeException(StatusCode.INVALID_FUNCTION, "Function does not exists");
		} else if (code == StatusCode.INVALID_ARGUMENTS.getValue()) {
			throw new DatabridgeException(StatusCode.INVALID_ARGUMENTS, "Invalid number of arguments");
		} else if (code == StatusCode.NOT_SUPPORT_SINGLE_PARTITION.getValue()) {
			throw new DatabridgeException(StatusCode.NOT_SUPPORT_SINGLE_PARTITION, "Missing partition by in window function");
		} else if (code == StatusCode.MISSING_ORDER_BY.getValue()) {
			throw new DatabridgeException(StatusCode.MISSING_ORDER_BY, "Missing order by in window function");
		} else if (code == StatusCode.NOT_SUPPORT_AGGREGATE.getValue()) {
			throw new DatabridgeException(StatusCode.NOT_SUPPORT_AGGREGATE, "Aggregate function does not supported");
		} else if (code == StatusCode.NOT_SUPPORT_WINDOW_FUNCTION.getValue()) {
			throw new DatabridgeException(StatusCode.NOT_SUPPORT_WINDOW_FUNCTION, "Window function does not supported in a table");
		} else if (code == StatusCode.INVALID_AGGREGATE_USAGE.getValue()) {
			throw new DatabridgeException(StatusCode.INVALID_AGGREGATE_USAGE,
				"It is not allowed to use an aggregate function in the argument of another aggregate function");
		} else if (code == StatusCode.FIELD_TITLE_TOO_LONG.getValue()) {
			throw new DatabridgeException(StatusCode.FIELD_TITLE_TOO_LONG, "Title of field is too long");
		} else if (code == StatusCode.HTTP_REQUEST_TIMEOUT.getValue()) {
			throw new DatabridgeException(StatusCode.HTTP_REQUEST_TIMEOUT, "REQUEST SERVICE TIMEOUT");
		} else {
			throw new DatabridgeException(StatusCode.FORMULA_ERROR, "Formula error");
		}
	}
}
