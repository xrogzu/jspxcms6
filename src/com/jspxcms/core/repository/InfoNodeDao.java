package com.jspxcms.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.InfoNode;

public interface InfoNodeDao extends Repository<InfoNode, Integer> {
	public List<InfoNode> findAll(Specification<InfoNode> spec, Sort sort);

	public List<InfoNode> findAll(Specification<InfoNode> spec, Limitable limit);

	public InfoNode findOne(Integer id);

	public InfoNode save(InfoNode bean);

	public void delete(InfoNode bean);

	public void delete(Iterable<InfoNode> beans);

	// --------------------

	@Modifying
	@Query("update InfoNode bean set bean.node.id=?2 where bean.node.id in (?1)")
	public int moveByNodeId(Collection<Integer> nodeIds, Integer nodeId);

	@Modifying
	@Query("delete from InfoNode bean where bean.info.id=?1")
	public int deleteByInfoId(Integer infoId);

	@Modifying
	@Query("delete from InfoNode bean where bean.node.id=?1")
	public int deleteByNodeId(Integer nodeId);
}
