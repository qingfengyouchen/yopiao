package com.zx.stlife.tools.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.zx.stlife.entity.goods.ShareGoods;

@Component
public class ShareGoodsValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ShareGoods.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ShareGoods shareGoods = (ShareGoods) target;

		if (shareGoods.getTitle().length() < 6) {
			errors.rejectValue("title", "301");
		}
		if (shareGoods.getTitle().length() > 32) {
			errors.rejectValue("title", "302");
		}
//		if (shareGoods.getContent().length() < 30) {
//			errors.rejectValue("content", "303");
//		}
//		if (shareGoods.getContent().length() > 300) {
//			errors.rejectValue("content", "304");
//		}
	}

}
