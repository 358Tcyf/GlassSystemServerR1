package project.ys.glass_system.service.impl;

import org.springframework.stereotype.Service;
import project.ys.glass_system.model.p.dao.FileDao;
import project.ys.glass_system.model.p.entity.File;
import project.ys.glass_system.service.FileService;

import javax.annotation.Resource;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileDao fileDao;

    @Override
    public void upload(File file) {
        fileDao.saveAndFlush(file);
    }

    @Override
    public void delete(File file) {
        fileDao.delete(file);
    }

    @Override
    public File getFile(String uuid) {
        return fileDao.findFileByUuid(uuid);
    }
}
