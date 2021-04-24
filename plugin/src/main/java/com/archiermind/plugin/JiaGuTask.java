package com.archiermind.plugin;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecSpec;

import javax.inject.Inject;
import java.io.File;

public class JiaGuTask extends DefaultTask {

    private File apkFile ;

    private JiaGuExt jiaGuExt;

    @Inject
    public JiaGuTask(File apkFile,JiaGuExt jiaGuExt){
        setGroup("jiagu");
        this.apkFile = apkFile;
        this.jiaGuExt = jiaGuExt;
    }

    @TaskAction
    public void abc(){

        System.out.println("############################################################");
        System.out.println("##########        print params configured          #########");
        System.out.println("##########        "+jiaGuExt.toString()+"          #########");
        System.out.println("############################################################");


        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {

                execSpec.commandLine("java","-jar",jiaGuExt.getJiaguToolPath()
                        ,"-login",jiaGuExt.getUserName(),jiaGuExt.getUserPwd() );
            }
        });


        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {

                execSpec.commandLine("java","-jar",jiaGuExt.getJiaguToolPath()
                        ,"-importsign",jiaGuExt.getKeyStorePath(),jiaGuExt.getKeyStorePass()
                        ,jiaGuExt.getKeyStoreAlias(),jiaGuExt.getGetKeyStoreAliasPwd() );
            }
        });


        getProject().exec(new Action<ExecSpec>() {
            @Override
            public void execute(ExecSpec execSpec) {

                execSpec.commandLine("java","-jar",jiaGuExt.getJiaguToolPath()
                        ,"-jiagu",apkFile.getAbsoluteFile(),apkFile.getParent(),"-autosign");
            }
        });

    }

}
