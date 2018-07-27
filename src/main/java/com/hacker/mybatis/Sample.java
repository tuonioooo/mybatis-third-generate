package com.hacker.mybatis;

import com.hacker.mybatis.config.po.Student;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


@Mojo(name = "sayhi", defaultPhase = LifecyclePhase.COMPILE)
public class Sample extends AbstractMojo {

    @Parameter
    private Student student;

    public void execute() throws MojoExecutionException {
        System.out.println("ok le ....");

        System.out.println("name：" + student.getName());
        System.out.println("age：" + student.getAge());

    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
    