package cn.itcast.bos.web.action.common;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.data.domain.Page;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 宝宝心里苦丶 on 2017/12/28.
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
    protected T model;

    protected  int page;
    protected  int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public T getModel() {
        return model;
    }
    public BaseAction(){
        try {


            //获得BaseAction 带有泛型的类型
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            System.out.println(type);
            //获得其泛型
            Class modelClass = (Class) type.getActualTypeArguments()[0];
            //创建对象
            model = (T) modelClass.newInstance();
            System.out.println(model);
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }
    protected  void pushPageDataToValueStack(Page pageData){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total",pageData.getTotalElements());
        map.put("rows",pageData.getContent());

        ActionContext.getContext().getValueStack().push(map);
    }
}
