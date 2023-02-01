package com.bookstore.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.admin.entity.settting.Setting;
import com.bookstore.admin.entity.settting.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {

	public List<Setting> findByCategory(SettingCategory category);
}
