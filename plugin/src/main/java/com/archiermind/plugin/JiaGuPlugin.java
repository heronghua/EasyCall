package com.archiermind.plugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.api.BaseVariantOutput;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;

public class JiaGuPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        System.out.println("JiaGuPlugin applied");

        final JiaGuExt jiagu = project.getExtensions().create("jiagu", JiaGuExt.class);

        project.afterEvaluate(new Action<Project>() {

            @Override
            public void execute(final Project project) {
                System.out.println(jiagu.getUserName());

                final AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);

                System.out.println(appExtension.getCompileSdkVersion());

                appExtension.getApplicationVariants().all(new Action<ApplicationVariant>() {
                    @Override
                    public void execute(ApplicationVariant applicationVariant) {
                        applicationVariant.getOutputs().all(new Action<BaseVariantOutput>() {
                            @Override
                            public void execute(BaseVariantOutput baseVariantOutput) {
                                File outputFile = baseVariantOutput.getOutputFile();
                                String name = baseVariantOutput.getName();

                                System.out.println("outputFile : " + outputFile + "name" + name);
                                if ("debug".equals(name)){
                                    project.getTasks().create("jiagu"+name,JiaGuTask.class,outputFile,jiagu);
                                }
                            }
                        });
                    }
                });


            }

        });
    }

}
