package project.ys.glass_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.model.p.dao.UserDao;
import project.ys.glass_system.model.p.entity.User;
import project.ys.glass_system.service.impl.FileServiceImpl;
import project.ys.glass_system.service.impl.UserServiceImpl;
import project.ys.glass_system.util.FileUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static project.ys.glass_system.constant.HttpConstant.FILE;
import static project.ys.glass_system.constant.HttpConstant.UPLOAD;
import static project.ys.glass_system.util.UuidUtil.getUUID32;


@Controller
@RequestMapping(FILE)
public class FileController {


    @Resource
    private FileServiceImpl fileService;

    @Resource
    private UserServiceImpl userService;


    @Resource
    private UserDao userDao;

    @RequestMapping(UPLOAD)
    @ResponseBody
    public RetResult upload(@RequestParam("image") MultipartFile file, String account) throws IOException {
        if (file.isEmpty())
            return RetResponse.makeErrRsp("文件不存在");
        if (!userService.isExisted(account))
            return RetResponse.makeErrRsp("用户不存在");
        User user = userDao.findByNo(account);
        String uuid = getUUID32();
        String filename = file.getOriginalFilename();
        project.ys.glass_system.model.p.entity.File userFile;
        if (user.getPic() == null)
            userFile = new project.ys.glass_system.model.p.entity.File();
        else
            userFile = user.getPic();
        userFile.setUuid(uuid);
        userFile.setName(filename);
        userFile.setPath(FileUtil.upload(file, uuid));
        user.setPic(userFile);
        fileService.upload(userFile);
        return RetResponse.makeOKRsp("上传成功");
    }

    @RequestMapping("/{account:.+}")
    public void getPic(@PathVariable String account, HttpServletResponse response) throws IOException {
        getPicWithTime(account, "", response);
    }

    @RequestMapping("/{account:.+}" + "/{time:.+}")
    public void getPicWithTime(@PathVariable String account, @PathVariable String time, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        byte[] data;
        if (!userService.isExisted(account)) {
            System.out.println("用户不存在");
            return;
        }
        User user = userDao.findByNo(account);
        String path = user.getPic().getPath();
        File pic = FileUtil.download(path);
        InputStream inputStream = new FileInputStream(pic);
        data = new byte[inputStream.available()];
        inputStream.read(data);
        inputStream.close();
        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }


}
