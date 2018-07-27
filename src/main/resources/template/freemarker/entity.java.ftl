package ${package.Entity};

<#if table.hasDate??>
import java.util.Date;
</#if>
import java.io.Serializable;

/**
 * ${table.comment}
 * @author ${author}
 * @date ${date}
 */
public class ${entity}{
<#list table.fields as field>
	<#if field.comment??>
	/** ${field.comment} */
	</#if>
	private ${field.propertyType} ${field.propertyName};
</#list>

	public ${entity}(){

	}

<#list table.fields as field>
	public ${field.propertyType} get${field.capitalName}(){
		return ${field.propertyName};
	}

	public void set${field.capitalName}(${field.propertyType} ${field.propertyName}){
		this.${field.propertyName} = ${field.propertyName};
	}
</#list>
}