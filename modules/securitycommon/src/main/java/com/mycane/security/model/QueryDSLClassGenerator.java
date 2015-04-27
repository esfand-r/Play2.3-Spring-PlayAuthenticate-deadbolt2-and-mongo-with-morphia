package com.mycane.security.model;

import com.mysema.query.annotations.*;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.codegen.JavaTypeMappings;
import org.mongodb.morphia.annotations.Entity;

import java.io.File;

/**
 * Created by esfandiaramirrahimi on 2015-05-06.
 * All this class does is use querydsl's generic exported to generate classes based on Morphia annotations.
 */
public class QueryDSLClassGenerator {

    public static void main(String[] args) {
        GenericExporter genericExporter = new GenericExporter();
        genericExporter.setTargetFolder(new File(args[0]));
        genericExporter.setEntityAnnotation(Entity.class);
        genericExporter.setSkipAnnotation(QueryTransient.class);
        genericExporter.setSupertypeAnnotation(QuerySupertype.class);
        genericExporter.setEmbeddableAnnotation(QueryEmbeddable.class);
        genericExporter.setEmbeddedAnnotation(QueryEmbedded.class);
        genericExporter.setSkipAnnotation(QueryExclude.class);

        genericExporter.setSerializerClass(EntitySerializer.class);
        genericExporter.setTypeMappingsClass(JavaTypeMappings.class);
        genericExporter.setCreateScalaSources(true);

        genericExporter.export("com.mycane.security.model");
    }
}
