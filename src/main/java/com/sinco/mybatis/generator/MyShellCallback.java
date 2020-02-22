package com.sinco.mybatis.generator;

import java.io.File;
import java.io.IOException;

import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import com.sinco.common.utils.FileUtils;

public class MyShellCallback extends DefaultShellCallback{
	private boolean generatorMapperJava;
	

    /**
     *  
     */
    public MyShellCallback(boolean overwrite,boolean generatorMapperJava) {
        super(overwrite);
        this.generatorMapperJava=generatorMapperJava;
    }
    
    public boolean isMergeSupported() {
        return true;
    }

	@Override
    public String mergeJavaFile(String newFileSource,
            File existingFile, String[] javadocTags, String fileEncoding)
            throws ShellException {
    	if(generatorMapperJava){
    		return newFileSource;
    	}
    	if(existingFile.getName().endsWith("Mapper.java")){
    		try {
				return FileUtils.readFileToString(existingFile, fileEncoding);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
    	}else{
    		return newFileSource; 
    	}
    }
}
