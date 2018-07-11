package com.mmall.controller.backend;


import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.mmall.util.Util.isAdmin;

@Controller
@RequestMapping(value = "/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession httpSession, Product product) {
        ServerResponse response = isAdmin(httpSession);
        if (!response.isSuccess()) {
            return response;
        }
        return iProductService.saveOrUpdateProduct(product);

    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession httpSession, Integer productId, Integer status) {
        ServerResponse response = isAdmin(httpSession);
        if (!response.isSuccess()) {
            return response;
        }
        return iProductService.setSaleStatus(productId, status);
    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession httpSession, Integer productId) {
        ServerResponse response = isAdmin(httpSession);
        if (!response.isSuccess()) {
            return response;
        }

        //todo
        return iProductService.manageProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession httpSession,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse response = isAdmin(httpSession);
        if (!response.isSuccess()) {
            return response;
        }

        //todo
        return iProductService.getProductList(pageNum, pageSize);
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession httpSession, String productName, Integer productId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse response = isAdmin(httpSession);
        if (!response.isSuccess()) {
            return response;
        }

        //todo
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession httpSession,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        Map resMap = Maps.newHashMap();
        ServerResponse serverResponse = isAdmin(httpSession);
        if (!serverResponse.isSuccess()) {
            resMap.put("success", false);
            resMap.put("msg", "没有权限处理此操作");
            return resMap;
        }

        //富文本对返回值有自己的要求，我们使用的是simditor的要求，需要按照 simditor 的要求返回
        //http://simditor.tower.im/docs/doc-config.html
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        // webapp/upload
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);

        if (StringUtils.isBlank(targetFileName)) {
            resMap.put("success", false);
            resMap.put("msg", "上传失败");

            return resMap;
        }

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resMap.put("success", true);
        resMap.put("msg", "上传成功");
        resMap.put("file_path", url);
        //富文本插件的要求
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resMap;
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession httpSession, @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request) {
        ServerResponse response = isAdmin(httpSession);
        if (!response.isSuccess()) {
            return response;
        }

        // webapp/upload
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);

        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }

}
