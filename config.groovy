def project = 'VladimirZubenko/jenkinstest'
def branchApi = new URL("https://api.github.com/repos/${project}/branches")
def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())
branches.each {
    def branchName = it.name
    def jobName = "${project}-${branchName}".replaceAll('/','-')
    job(jobName) {
        scm {
            git("git://github.com/${project}.git", branchName)
        }
        steps {
            msBuild {
                msBuildInstallation('MSBuild 1.8')
                buildFile('dir1/build.proj')
                args('check')
                args('another')
                passBuildVariables()
                continueOnBuildFailure()
                unstableIfWarnings()
            }
            msBuild {
                msBuildInstallation('MSBuild 1.8')
                buildFile('dir1/build_demo.proj')
                args('check')
                args('another')
                passBuildVariables()
                continueOnBuildFailure()
                unstableIfWarnings()
            }
            maven("test -Dproject.name=${project}/${branchName}")
        }
    }
}