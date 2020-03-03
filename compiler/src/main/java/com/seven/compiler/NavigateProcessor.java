package com.seven.compiler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.seven.annotation.ActivityDestination;
import com.seven.annotation.FragmentDestination;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;


@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.seven.annotation.FragmentDestination", "com.seven.annotation.ActivityDestination"})
public class NavigateProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private static final String OUTPUT_FILE_NAME = "destination.json";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> fragmentElements = roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);
        Set<? extends Element> activityElements = roundEnvironment.getElementsAnnotatedWith(ActivityDestination.class);
        if (!fragmentElements.isEmpty() || !activityElements.isEmpty()) {
            HashMap<String, JSONObject> destinationMap = new HashMap<>();
            handleDestination(fragmentElements, FragmentDestination.class, destinationMap);
            handleDestination(activityElements, ActivityDestination.class, destinationMap);
            FileOutputStream fos = null;
            OutputStreamWriter writer = null;
            try {
                FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
                String resourcePath = resource.toUri().getPath();
                messager.printMessage(Diagnostic.Kind.NOTE, "resourcePath:" + resourcePath);

                String appPath = resourcePath.substring(0, resourcePath.indexOf("app") + 4);
                String assetsPath = appPath + "src/main/assets/";

                File file = new File(assetsPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outPutFile = new File(file, OUTPUT_FILE_NAME);
                if (outPutFile.exists()) {
                    outPutFile.delete();
                }
                outPutFile.createNewFile();

                String content = JSON.toJSONString(destinationMap);
                fos = new FileOutputStream(outPutFile);
                writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                writer.write(content);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private void handleDestination(Set<? extends Element> elements, Class<? extends Annotation> annotationClass, HashMap<String, JSONObject> destinationMap) {
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            String className = typeElement.getQualifiedName().toString();
            String pageUrl = null;
            int id = Math.abs(className.hashCode());
            boolean isLogin = false;
            boolean isStart = false;
            boolean isFragment = false;
            Annotation annotation = element.getAnnotation(annotationClass);
            if (annotation instanceof FragmentDestination) {
                FragmentDestination destination = (FragmentDestination) annotation;
                pageUrl = destination.pageUrl();
                isLogin = destination.isLogin();
                isStart = destination.isStart();
                isFragment = true;
            } else if (annotation instanceof ActivityDestination) {
                ActivityDestination destination = (ActivityDestination) annotation;
                pageUrl = destination.pageUrl();
                isLogin = destination.isLogin();
                isStart = destination.isStart();
                isFragment = false;
            }
            if (destinationMap.containsKey(pageUrl)) {
                messager.printMessage(Diagnostic.Kind.ERROR, "不允许使用相同pageUrl" + className);
            } else {
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("pageUrl", pageUrl);
                object.put("className", className);
                object.put("isLogin", isLogin);
                object.put("isStart", isStart);
                object.put("isFragment", isFragment);
                destinationMap.put(pageUrl,object);
            }
        }

    }
}
