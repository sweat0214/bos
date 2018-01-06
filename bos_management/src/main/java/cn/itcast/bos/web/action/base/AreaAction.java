package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/26.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {
    @Autowired
    private AreaService areaService;

    //接受上传文件
    private File file;
    private String fileFileName;
    private String fileContentType;

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setFile(File file) {
        this.file = file;
    }

    //批量区域数据导入
    @Action(value = "area_bacthImport",results = {@Result(name = "success",type = "json")})
    public String area_bacthImport() {
        String msg = "";
        try {
            Workbook workbook = null;
            //0判断后缀名
            if (fileFileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else if (fileFileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }
            List<Area> areas = new ArrayList<Area>();
            //1.创建workBook对象
            //2.获得sheet
            Sheet sheet = workbook.getSheetAt(0);
            //3.获得row行
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                //获得每一行
                Row row = sheet.getRow(i);
                //获得每一个单元格 cell 并且获得值
                String id = row.getCell(0).getStringCellValue();
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();

                //基于pinyin4j 生成城市编码和简码
                String p = province.substring(0,province.length()-1);
                String c = city.substring(0,city.length() - 1);
                String d = district.substring(0,district.length() - 1);
                //简码
                String[] headArray = PinYin4jUtils.getHeadByString(p + c + d);
                StringBuffer buffer = new StringBuffer();
                for (String headstr : headArray) {
                    buffer.append(headstr);
                }
                String shortcode = buffer.toString();
                //城市编码
                String citycode = PinYin4jUtils.hanziToPinyin(city, "");

                Area area = new Area(id, province, city, district, postcode,citycode,shortcode);


                areas.add(area);

            }
            //保存数据
            areaService.saveBatch(areas);

            //成功!
            msg = "上传成功";

        } catch (Exception e) {
            e.printStackTrace();
            msg = "上传失败!";
        }
        ActionContext.getContext().getValueStack().push(msg);

        return "success";
    }

    @Action(value = "area_pageQuery",results = {@Result(name = "success",type = "json")})
    //条件查寻方法
    public String pagaQuery(){
        //查寻数据
        Page pageData = areaService.findPageData(model, page, rows);
        //压入值栈返回
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    @Action(value="area_findAll",results=@Result(name="success",type="json"))
    public String findAll(){
        //查询数据
        List<Area> areas = areaService.findAll();
        //压栈
        ActionContext.getContext().getValueStack().push(areas);
        return SUCCESS;
    }
    //区域添加方法
    @Action(value = "area_save",results = {@Result(name = "success",location = "./pages/base/area.html",type = "redirect")})
    public String save(){
        areaService.save(model);
        return SUCCESS;
    }

    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    //区域删方法
    @Action(value = "area_delete",results = {@Result(name = "success",location = "./pages/base/area.html",type = "redirect")})
    public String delete(){
        String[] idArray = ids.split(",");
        System.out.println(idArray.toString());
        areaService.deleteAreaById(idArray);
        return SUCCESS;
    }

    /**
     * 导出功能
     * @return 文件的二进制
     */
    @Action(value="area_export")
    public String export(){

        //1.查询所有数据
        List<Area> areas = areaService.findAll();

        /*List<Area> areas = new ArrayList<Area>();

        for (int i = 0; i < 100020; i++) {
            areas.add(new Area("qy"+i, "陕西"+i, "汉中"+i, "汉台区"+i, "723000"));
        }*/

        //2.将数据写入excel文件
        //2.1创建wookbook对象
        Workbook workbook = new HSSFWorkbook();

        //规定5000条数据一个sheet
        int size = areas.size();
        int pageSize = 5000;
        //总的sheet数
        int count = (size % pageSize) == 0 ? size/pageSize : (size/pageSize+1);

        for (int i = 0; i < count; i++) {
            //2.2创建Sheet
            Sheet sheet = workbook.createSheet("区域数据"+i);
            //2.3创建row 表头行
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("区域编号");
            row.createCell(1).setCellValue("省份");
            row.createCell(2).setCellValue("城市");
            row.createCell(3).setCellValue("区域");
            row.createCell(4).setCellValue("邮编");

            //2.4遍历集合，创建数据行
            for (int j = i*pageSize; j < (i + 1)* pageSize; j++) {

                if(j >= size){
                    break;
                }
                Row row1 = sheet.createRow(sheet.getLastRowNum() + 1);
                //alt+shift +a
                row1.createCell(0).setCellValue(areas.get(j).getId());
                row1.createCell(1).setCellValue(areas.get(j).getProvince());
                row1.createCell(2).setCellValue(areas.get(j).getCity());
                row1.createCell(3).setCellValue(areas.get(j).getDistrict());
                row1.createCell(4).setCellValue(areas.get(j).getPostcode());
            }
        }

        try {
            //3.将excel文件写到客户端。提供文件下载的操作
            String filename = "qysj.xls";

            ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
            //一个流，两个头 content-type,
            ServletActionContext.getResponse().setContentType(ServletActionContext.getServletContext().getMimeType(filename));
            //content-disposition
            ServletActionContext.getResponse().setHeader("content-disposition", "attachment;filename="+filename);
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NONE;
    }


}
