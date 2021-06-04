package com.haizhi.databridge.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.TTableBean;

@Transactional
@Repository
public interface TTableRepository extends HaizhiBaseRepository<TTableBean, String> {

	Optional<List<TTableBean>> findAllByDbIdAndOwner(String dbId, String owner);
}
