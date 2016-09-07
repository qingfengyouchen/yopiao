package com.zx.stlife.repository.jpa.goods;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.goods.ShareImage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShareImageDao extends MyJpaRepository<ShareImage, Integer>{

	@Query("select t from ShareImage t where t.shareGoods.id=?1 and t.state=?2")
	List<ShareImage> findByShareGoods(Integer shareGoodsId, Byte state);

	@Query("select t from ShareImage t where t.shareGoods.id=?1 order by t.createTime desc")
	List<ShareImage> findShareImage(Integer shareGoodsId);

	@Modifying
	@Query("update ShareImage t set t.state =?1 where t.id in(?2)")
	public int deleteByIds(Byte state, List<Integer> ids);
	
	@Modifying
	@Query("update ShareImage t set t.state =?1 where t.shareGoods.id in(?2)")
	int deleteByShareGoodsId(Byte state, List<Integer> ids);

	@Query(nativeQuery = true,
			value = "select url from goods_share_image where goods_share_id=?1 and state=?2 order by id limit 1")
	String getFirstImageUrl(Integer shareGoodsId, Byte state);
}
