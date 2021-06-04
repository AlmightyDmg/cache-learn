package com.haizhi.databridge.web.controller.base;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 支持对某个表进行CRUD
 * 
 * @param <T>
 * @param <ID>
 */
public abstract class BaseCrudController<T, ID> extends BaseCrossDomainController {

	public abstract JpaRepository<T, ID> getRepo();

	@GetMapping({ "" })
	protected ResponseEntity<Page<T>> page(T example, Pageable page) {
		ExampleMatcher em = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);

		return ResponseEntity.ok(this.getRepo().findAll(Example.of(example, em), page));
	}

	@GetMapping({ "/list" })
	protected ResponseEntity<List<T>> list(T example) {
		ExampleMatcher em = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING);

		return ResponseEntity.ok(this.getRepo().findAll(Example.of(example, em)));
	}

	@GetMapping({ "/{id:\\d+}" })
	protected ResponseEntity<T> get(@PathVariable("id") ID id) {
		return ResponseEntity.of(this.getRepo().findById(id));
	}

	/**
	 * 默认使用requestBody 传入json格式
	 * 
	 * @param bean
	 * @param errors
	 * @return
	 */
	@PostMapping(value = { "/body" }, consumes = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<T> saveBeanWithBody(@Valid @RequestBody T bean, Errors errors) {
		check(errors);
		return ResponseEntity.ok(this.getRepo().save(bean));
	}

	/**
	 * 默认使用 form参数 或者 query参数 传入保存
	 *
	 * 默认参数注入bean query,path, form会混合在一起 body的会单独被隔离
	 * 
	 * @param bean
	 * @param errors
	 * @return
	 */
	@PostMapping(value = { "", "/form" })
	protected ResponseEntity<T> saveBean(@Valid T bean, Errors errors) {
		check(errors);
		return ResponseEntity.ok(this.getRepo().save(bean));
	}

	@Data
	public class BeanList {
		private List<T> beans;
	}

	/**
	 * 批量保存 用requestBody json Array格式
	 * 
	 * @param beanList
	 * @param errors
	 * @return
	 */
	@PostMapping(value = { "/batch" }, consumes = MediaType.APPLICATION_JSON_VALUE)
	protected ResponseEntity<?> batchSaveBeanWithBody(@Valid @RequestBody BeanList beanList, Errors errors) {
		check(errors);
		return ResponseEntity.ok(this.getRepo().saveAll(beanList.getBeans()));
	}

}
