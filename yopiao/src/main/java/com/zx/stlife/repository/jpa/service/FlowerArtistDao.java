package com.zx.stlife.repository.jpa.service;

import com.base.jpa.MyJpaRepository;
import com.zx.stlife.entity.service.FlowerArtist;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**鲜花速递**/
public interface FlowerArtistDao  extends MyJpaRepository<FlowerArtist, Integer>{

	@Query("select f from FlowerArtist f where f.state=?1 order by f.id desc")
	public List<FlowerArtist> selListFlowerArtist(Byte state);
}
