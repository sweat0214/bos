package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
import cn.itcast.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 宝宝心里苦丶 on 2017/12/28.
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea> {
    @Autowired
    private SubAreaService subAreaService;
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
    @Action(value = "subArea_save",results = {@Result(name = "success",type = "redirect",location = "./pages/base/sub_area.html")})
    public String  save(){
        subAreaService.save(model);
        return SUCCESS;
    }
    @Action(value = "subArea_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        Page p = subAreaService.findPageQuery(model, page, rows);
        pushPageDataToValueStack(p);
        return SUCCESS;
    }
    @Action(value = "subArea_batchImport", results = @Result(name = "success", type = "json"))
    public String importXls() {

        String msg = "";
        try {
            Workbook workbook = null;

            // 0.判断后缀名
            if (fileFileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else if (fileFileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }

            List<SubArea> subAreas = new ArrayList<SubArea>();

            // 1.创建WorkBook对象

            // 2.获取Sheet页
            Sheet sheet = workbook.getSheetAt(0);
            // 3.获取row行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                SubArea subArea = new SubArea();
                // 获取每一行
                Row row = sheet.getRow(i);

                // 获取每一个单元格 cell 并且获取值
                String id = row.getCell(0).getStringCellValue();
                subArea.setId(id);

                //fixedId
                String fixedId = row.getCell(1).getStringCellValue();
                FixedArea fixedArea = new FixedArea();
                fixedArea.setId(fixedId);
                subArea.setFixedArea(fixedArea);

                //areaId
                String areaId = row.getCell(2).getStringCellValue();
                Area area = new Area();
                area.setId(areaId);
                subArea.setArea(area);


                String keyWords = row.getCell(3).getStringCellValue();
                subArea.setKeyWords(keyWords);
                String startNum = row.getCell(4).getStringCellValue();
                subArea.setStartNum(startNum);
                String endNum = row.getCell(5).getStringCellValue();
                subArea.setEndNum(endNum);

                String single = row.getCell(6).getStringCellValue();
                subArea.setSingle(single.charAt(0));
                String assistKeyWords = row.getCell(7).getStringCellValue();
                subArea.setAssistKeyWords(assistKeyWords);


                subAreas.add(subArea);

            }
            // 保存数据
            subAreaService.importXls(subAreas);
            //成功！！！
            msg = "上传成功！";
        } catch (Exception e) {
            e.printStackTrace();

            msg = "上传失败！";
        }

        ActionContext.getContext().getValueStack().push(msg);

        return SUCCESS;
    }


}
