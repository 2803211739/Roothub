package wang.miansen.roothub.modules.security.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wang.miansen.roothub.common.beans.Page;
import wang.miansen.roothub.common.dao.BaseDao;
import wang.miansen.roothub.common.dao.mapper.wrapper.query.QueryWrapper;
import wang.miansen.roothub.common.service.impl.AbstractBaseServiceImpl;
import wang.miansen.roothub.common.util.BeanUtils;
import wang.miansen.roothub.common.util.StringUtils;
import wang.miansen.roothub.modules.security.dao.ResourceDao;
import wang.miansen.roothub.modules.security.dto.ResourceCategoryDTO;
import wang.miansen.roothub.modules.security.dto.ResourceDTO;
import wang.miansen.roothub.modules.security.model.Resource;
import wang.miansen.roothub.modules.security.model.ResourceCategory;
import wang.miansen.roothub.modules.security.service.ResourceCategoryService;
import wang.miansen.roothub.modules.security.service.ResourceService;

/**
 * 资源 Service Impl
 * 
 * @author miansen.wang
 * @date 2020-03-13
 */
@Service
public class ResourceServiceImpl extends AbstractBaseServiceImpl<Resource, ResourceDTO> implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;

	@Autowired
	private ResourceCategoryService resourceCategoryService;

	@Override
	public Function<? super ResourceDTO, ? extends Resource> getDTO2DOMapper() {
		return resourceDTO -> (Resource) BeanUtils.DTO2DO(resourceDTO, Resource.class);
	}

	@Override
	public Function<? super Resource, ? extends ResourceDTO> getDO2DTOMapper() {
		return resource -> (ResourceDTO) BeanUtils.DO2DTO(resource, ResourceDTO.class);
	}

	@Override
	public BaseDao<Resource> getDao() {
		return resourceDao;
	}

	@Override
	public Page<ResourceDTO> pageByNameOrCategoryName(Integer pageNumber, String resourceName,
			String resourceCategoryName) {
		Page<ResourceDTO> page = new Page<>(new ArrayList<>(), pageNumber, 25, 0);
		QueryWrapper<Resource> q1 = new QueryWrapper<>();
		if (StringUtils.notEmpty(resourceCategoryName)) {
			QueryWrapper<ResourceCategory> q2 = new QueryWrapper<>();
			q2.like("resource_category_name", resourceCategoryName);
			ResourceCategoryDTO resourceCategoryDTO = resourceCategoryService.getOne(q2);
			if (resourceCategoryDTO == null) {
				return null;
			}
			q1.eq("resource_category_id", resourceCategoryDTO.getResourceCategoryId());
		}
		if (StringUtils.notEmpty(resourceName)) {
			q1.like("resource_name", resourceName);
		}
		page = page(pageNumber, 25, q1);
		return page;
	}

}