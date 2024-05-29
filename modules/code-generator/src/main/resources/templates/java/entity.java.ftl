package ${package.Entity};

<#list table.importPackages as importPackage>
import ${importPackage};
</#list>
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * ${table.comment} 实体类
 *
 * @author ${author}
 * @date ${date}
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name="${table.name}")
<#if superEntityClass??>
public class ${table.entityName} extends ${superEntityClass} {
<#elseif entitySerialVersionUID>
public class ${table.entityName} implements Serializable {
<#else>
public class ${table.entityName} {
</#if>
<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- BEGIN 字段循环 -->
<#list table.fields as field>
    /**
     * ${field.comment}
     */
    <#if field.hasPk>
    @Id
    <#if field.autoIncrement>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    <#elseif idType??>
    @${idType}
    </#if>
    </#if>
    <#if field.keywords>
    @Column(name= "${field.columnName}",columnDefinition = "${field.type}")
    </#if>
    <#if field.required>
    <#if field.propertyType == "String">
    @NotBlank(message = "${field.comment}不能为空")
    <#else>
    @NotNull(message = "${field.comment}不能为空")
    </#if>
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#-- END 字段循环 -->
}