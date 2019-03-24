package project.ys.glass_system.service;


import project.ys.glass_system.model.p.entity.File;

public interface FileService {

    void upload(File file);

    void delete(File file);

    File getFile(String uuid);
}
