package com.vizor.test;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Photo implements Serializable {

    protected String Image;
    protected String Title;
    protected String Annotation;

}
