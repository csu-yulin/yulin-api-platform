package ${packageName}.client.${subPackageName};

import ${packageName}.client.AbstractResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
<#-- 导入必要的类 -->
<#list imports as imp>
import ${imp};
</#list>

/**
 * @author 刘飘
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ${className} extends AbstractResponse implements Serializable{
    @Serial
    private static final long serialVersionUID=1L;

<#-- 定义模板变量，用于填充生成的字段 -->
<#list fields as field>
    /**
     * ${field.comment}
     */
    <#list field.annotations as annotation>
        ${annotation}
    </#list>
    private ${field.type} ${field.name};
</#list>
}