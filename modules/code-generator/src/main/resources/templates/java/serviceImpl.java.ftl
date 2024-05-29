package ${package.ServiceImpl};

import ${package.Entity}.${table.entityName};
import ${package.Repository}.${table.repositoryName};
<#if generateService>
import ${package.Service}.${table.serviceName};
</#if>
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * ${table.comment} 业务实现类
 *
 * @author ${author}
 * @date ${date}
 */
 @Service
 public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.repositoryName},${table
 .entityName},${table.pkType}><#if generateService> implements ${table.serviceName}</#if> {
 }