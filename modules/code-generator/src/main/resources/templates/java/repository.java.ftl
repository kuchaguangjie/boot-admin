package ${package.Repository};

import ${package.Entity}.${table.entityName};
<#if superRepositoryClass??>
import ${superRepositoryClassPackage};
<#else >
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
</#if>
import org.springframework.stereotype.Repository;

/**
 * ${table.comment} Repository
 *
 * @author ${author}
 * @date ${date}
 */
@Repository
<#if superRepositoryClass??>
public interface ${table.repositoryName} extends ${superRepositoryClass}<${table.entityName}, ${table.pkType}> {
<#else >
public interface ${table.repositoryName} extends JpaRepository<${table.entityName}, ${table.pkType}>, JpaSpecificationExecutor<${table.entityName}> {
</#if>
}