package com.supplyai.mapper;

import com.supplyai.dto.OrganizationDTO;
import com.supplyai.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    OrganizationDTO organizationToOrganizationDTO(Organization organization);

    Organization organizationDTOToOrganization(OrganizationDTO organizationDTO);
}
