package ${package.Response};

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* <p> ${table.comment}数据返回封装 </p>
*
* @author daizhao ${currentTime}
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ${entity}Response{
<#list table.fields as field>
    <#if field.propertyType == "Date">
        <#if field.comment??>
        /** ${field.comment} */
        </#if>
        private LocalDateTime ${field.propertyName};
    <#elseif field.propertyName == "remove">
    <#else>
        <#if field.comment??>
        /** ${field.comment} */
        </#if>
        private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
}
