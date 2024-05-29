package com.hb0730.code.generator.core.config;

import com.hb0730.base.utils.StrUtil;
import com.hb0730.code.generator.core.enums.ConstVal;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * package配置
 *
 * @author <a href="mailto:huangbing0730@gmail">hb0730</a>
 * @date 2024/5/28
 */
@Getter
public class PackageConfig {

    private String parent = "com.hb0730";
    private String moduleName = "";
    private String entity = "domain.entity";
    private String dto = "domain.dto";
    private String vo = "domain.vo";
    private String query = "domain.query";
    private String repository = "repository";
    private String service = "service";
    private String serviceImpl = "service.impl";
    private String controller = "controller";

    private final Map<String, String> packageInfo = new HashMap<>();


    public String getParent() {
        return StrUtil.isNotBlank(moduleName) ? parent + StrUtil.DOT + moduleName : parent;
    }

    /**
     * 拼接包名
     *
     * @param subPackage 子包名
     * @return 包名
     */
    public String joinPackage(String subPackage) {
        String parent = getParent();
        if (StrUtil.isBlank(parent)) {
            return subPackage;
        }
        return getParent() + StrUtil.DOT + subPackage;
    }

    public Map<String, String> getPackageInfo() {
        if (packageInfo.isEmpty()) {
            packageInfo.put(ConstVal.PACKAGE_ENTITY, this.joinPackage(this.getEntity()));
            packageInfo.put(ConstVal.PACKAGE_DTO, this.joinPackage(this.getDto()));
            packageInfo.put(ConstVal.PACKAGE_VO, this.joinPackage(this.getVo()));
            packageInfo.put(ConstVal.PACKAGE_QUERY, this.joinPackage(this.getQuery()));
            packageInfo.put(ConstVal.PACKAGE_REPOSITORY, this.joinPackage(this.getRepository()));
            packageInfo.put(ConstVal.PACKAGE_SERVICE, this.joinPackage(this.getService()));
            packageInfo.put(ConstVal.PACKAGE_SERVICE_IMPL, this.joinPackage(this.getServiceImpl()));
            packageInfo.put(ConstVal.PACKAGE_CONTROLLER, this.joinPackage(this.getController()));
            packageInfo.put(ConstVal.PACKAGE_MODULE_NAME, this.getModuleName());
            packageInfo.put(ConstVal.PACKAGE_PARENT, this.getParent());
        }
        return packageInfo;
    }

    /**
     * 获取包名
     *
     * @param moduleKey 模块名
     * @return 包名
     */
    public String getPackage(String moduleKey) {
        return getPackageInfo().get(moduleKey);
    }


    public static class Builder implements IBuilder<PackageConfig> {
        private final PackageConfig packageConfig = new PackageConfig();

        public Builder() {
        }

        public Builder(String parent) {
            packageConfig.parent = parent;
        }

        public Builder(String parent, String moduleName) {
            packageConfig.parent = parent;
            packageConfig.moduleName = moduleName;
        }


        /**
         * 设置parent package
         *
         * @param parent .
         * @return .
         */
        public Builder parent(String parent) {
            packageConfig.parent = parent;
            return this;
        }

        /**
         * 设置moduleName
         *
         * @param moduleName .
         * @return .
         */
        public Builder moduleName(String moduleName) {
            packageConfig.moduleName = moduleName;
            return this;
        }

        /**
         * 设置entity
         *
         * @param entity .
         * @return .
         */
        public Builder entity(String entity) {
            packageConfig.entity = entity;
            return this;
        }

        /**
         * 设置dto
         *
         * @param dto .
         * @return .
         */
        public Builder dto(String dto) {
            packageConfig.dto = dto;
            return this;
        }

        /**
         * 设置vo
         *
         * @param vo .
         * @return .
         */
        public Builder vo(String vo) {
            packageConfig.vo = vo;
            return this;
        }

        /**
         * 设置query
         *
         * @param query .
         * @return .
         */
        public Builder query(String query) {
            packageConfig.query = query;
            return this;
        }

        /**
         * 设置repository
         *
         * @param repository .
         * @return .
         */
        public Builder repository(String repository) {
            packageConfig.repository = repository;
            return this;
        }

        /**
         * 设置service
         *
         * @param service .
         * @return .
         */
        public Builder service(String service) {
            packageConfig.service = service;
            return this;
        }

        /**
         * 设置serviceImpl
         *
         * @param serviceImpl .
         * @return .
         */
        public Builder serviceImpl(String serviceImpl) {
            packageConfig.serviceImpl = serviceImpl;
            return this;
        }

        /**
         * 设置controller
         *
         * @param controller .
         * @return .
         */
        public Builder controller(String controller) {
            packageConfig.controller = controller;
            return this;
        }


        @Override
        public PackageConfig build() {
            return this.packageConfig;
        }
    }

}
