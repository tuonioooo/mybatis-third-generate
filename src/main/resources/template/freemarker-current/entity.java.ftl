package ${package.Entity};

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

<#if table.hasDate??>
import java.util.Date;
</#if>
import java.io.Serializable;

/**
 * <p> ${table.comment} </p>
 *
 * @author daizhao ${currentTime}
 */
@Table(name = "${table.name}")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ${entity}Entity{
<#list table.fields as field>
	<#if field.comment??>
	/** ${field.comment} */
	</#if>
	@Column(name = "${field.name}")
	private ${field.propertyType} ${field.propertyName};
</#list>
}