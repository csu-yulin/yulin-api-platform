<#-- 设置默认输出格式 -->
package ${packageName}.client.${subPackageName};

import ${packageName}.client.AbstractRequest;
import ${packageName}.enums.HttpMethod;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘飘
 */
@EqualsAndHashCode(callSuper = true)
@Setter
public class ${className} extends AbstractRequest< ${className?replace("Request","Response")}> {

<#-- 定义模板变量，用于填充生成的字段 -->
<#list fields as field>
    /**
     * ${field.comment}
     */
    private ${field.type} ${field.name};
</#list>

    /**
     * 接口请求方法
     *
     * @return 请求方法
     */
    @Override
    public String getMethod(){
        return HttpMethod.${httpMethod}.getValue();
        }

    /**
     * 接口请求路径
     *
     * @return 请求路径
     */
    @Override
    public String getPath(){
        return"${path}" ;
        }

    /**
     * 接口请求参数
     *
     * @return 请求参数
     */
    @Override
    public Map<String, String> getApiParams(){
<#if httpMethod == "GET">
        return Map.of(
    <#list fields as field>
            "${field.name}", ${field.name}<#if field_has_next>,</#if>
    </#list>
               );
<#else>
            return null;
</#if>
        }

    /**
     * 请求实体
     *
     * @return 请求实体
     */
    @Override
    public Object getBody(){
                <#if httpMethod == "POST">
        return Map.of(
                    <#list fields as field>
            "${field.name}", ${field.name}<#if field_has_next>,</#if>
                    </#list>
               );
                <#else>
        return null;
                </#if>
        }

    /**
     * 请求响应实体
     *
     * @return 请求响应实体的Class对象
     */
    @Override
    public Class<${className?replace("Request","Response")}> getResponseClass(){
        return ${className?replace("Request","Response")}.class;
    }
            }