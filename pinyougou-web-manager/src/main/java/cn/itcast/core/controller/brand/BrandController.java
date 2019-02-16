package cn.itcast.core.controller.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.brand.BrandService;
import cn.itcast.core.utils.Excel.ExportExcelUtil;
import com.alibaba.dubbo.config.annotation.Reference;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<Brand> findAll() {
        return brandService.findAll();
    }

    /**
     * 品牌管理的分页查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        return brandService.findPage(pageNo, pageSize);
    }

    /**
     * 品牌管理条件查询
     *
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer pageNo, Integer pageSize, @RequestBody Brand brand) {
        return brandService.search(pageNo, pageSize, brand);
    }

    /**
     * 保存品牌
     *
     * @param brand
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Brand brand) {
        try {
            brandService.add(brand);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }

    /**
     * 品牌回显
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public Brand findOne(Long id) {
        return brandService.findOne(id);
    }

    /**
     * 更新品牌
     *
     * @param brand
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody Brand brand) {
        try {
            brandService.update(brand);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids) {
        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * <<<<<<< HEAD
     * 新增规格时初始化品牌列表
     * <p>
     * =======
     * 新增规格时初始化品牌列表4444444444444
     * >>>>>>> 2079a3bb234316c222fbeeb946c7c84205d7973b
     *
     * @return
     */
    @RequestMapping("/selectOptionList.do")
    public List<Map<String, String>> selectOptionList() {
        return brandService.selectOptionList();
    }

    /**
     * <<<<<<< HEAD
     * 导出报表
     *
     * @param response
     */
    @RequestMapping(value = "/export.do")
    public void export(HttpServletResponse response) {

        List<Brand> list = brandService.findAll();
        //excel标题
        String[] title = {"id", "名称", "首字母"};

        //sheet名
        String sheetName = "品牌信息表";

        String[][] content = new String[list.size()][3];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            Brand obj = list.get(i);
            content[i][0] = String.valueOf(obj.getId());
            content[i][1] = obj.getName();
            content[i][2] = obj.getFirstChar();
        }

        String filename = "品牌名称表" + new Date().getTime() + ".xlsx";

        ExportExcelUtil.getXSSFWorkbook(response, sheetName, title, content, filename);
    }

    /**
     * excel表导入
     *
     * @param upload_file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "import.do")
    @ResponseBody
    public Map import_excel_data(@RequestParam MultipartFile upload_file) throws Exception {
        Map<String, String> returnMap = new HashMap<>();
        List<Map<String, String>> data = null;

        InputStream inputStream = upload_file.getInputStream();

        try {
            data = ExportExcelUtil.parse(inputStream, ExportExcelUtil.EXCEL_FIRST_ROW_INDEX);
        } catch (Exception ex) {
            returnMap.put("error", "读取excel文件失败:" + ex.getMessage());
            throw new Exception("读取excel文件失败:" + ex.getMessage());
        }
        if (data == null) {
            returnMap.put("error", "读取excel失败,或excel为空\"");
            throw new Exception("读取excel失败,或excel为空");
        }

        for (Map<String, String> row : data) {
            String id = row.get("id");
            String name = row.get("名称");
            String first = row.get("首字母");

            Brand brand = new Brand();
            brand.setId(Long.valueOf(id));
            brand.setName(name);
            brand.setFirstChar(first);
            brandService.add(brand);

        }
        returnMap.put("true", "导入成功!");
        return returnMap;
    }

    /**
     * 审核商品
     *
     * @param ids
     */
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids, String status) {
        try {
            brandService.updateStatus(ids, status);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }
}
