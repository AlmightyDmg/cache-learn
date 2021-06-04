package com.haizhi.databridge.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.TDataBaseSourceBean;

@Transactional
@Repository
public interface TdataBaseSourceRepository extends HaizhiBaseRepository<TDataBaseSourceBean, String> {
	Optional<List<TDataBaseSourceBean>> findByDsNameAndOwner(String dsName, String owner);
	Optional<TDataBaseSourceBean> findByDbIdAndOwner(String dsName, String owner);

	@Query(value = "update TdataBaseSourceBean set deleted = 1 where db_id = ?1 and deleted=0", nativeQuery = true)
	void logicDeleteByDbId(String dbId);

}
