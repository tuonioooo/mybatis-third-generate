package ${package.Request};

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* <p> ${table.comment}数据请求封装 </p>
*
* @author daizhao ${currentTime}
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ${entity}Request extends BasePage {
<#list table.fields as field>
    <#if field.propertyType == "Date">
    <#elseif field.propertyName == "remove">
    <#else>
        <#if field.comment??>
        /** ${field.comment} */
        </#if>
        private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>

        /**
        * 操作人Id
        */
        private Integer opId;
        /**
        * 操作人姓名
        */
        private String opName;
        /**
        * 租户Id
        */
        private Integer tenantId;

        public interface Insert{

        }

        public interface Update{

        }

}
