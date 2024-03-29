package com.fmbank.welfarelottery.service;


public interface IFileFormatConvertService {
    boolean jpgToWebp(String oldFile, String newFile);

    boolean webpToJpg(String oldFile, String newFile);

}
