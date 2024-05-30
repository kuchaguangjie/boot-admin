package ${package.Dto};

<#if superDtoClass??>
import ${superDtoClassPackage};
</#if>
<#list table.dtoImportPackages as importPackage>
import ${importPackage};
</#list>
import lombok.Getter;
import lombok.Setter;
<#if dtoChainModel>
import lombok.experimental.Accessors;
</#if>

/**
 * ${table.comment} DTO
 *
 * @author ${author}
 * @date ${date}
 */
@Getter
@Setter
<#if dtoChainModel>
@Accessors(chain = true)
</#if>
<#if superDtoClass??>
public class ${table.dtoName} extends ${superDtoClass} {
<#elseif dtoSerialVersionUID>
public class ${table.dtoName} implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
<#else>
public class ${table.dtoName} {
</#if>

<#list table.fields as field>
   <#if field.comment??>
    /**
     * ${field.comment}
     */
   </#if>
   <#if springdoc>
    @Schema(description = "${field.comment}")
   </#if>
   <#if field.required>
      <#if field.propertyType == "String">
    @NotBlank(message = "${field.comment}不能为空")
      <#else>
    @NotNull(message = "${field.comment}不能为空")
      </#if>
   </#if>
   <#if field.propertyType =="Date" >
       <#if dtoDatePattern??>
    @JsonFormat(pattern = "${dtoDatePattern}", timezone = "GMT+8")
        </#if>
   </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
}