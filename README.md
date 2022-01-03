# Samurai Style Cop 2 (for Java projects)
- Have a massive legacy codebase that has no styling standards?  
- Want to add a code style standard without bulk reformatting tons of code that can cause mass merge conflicts and headache downstream?  
- Want to lint check newly staged/modified code only?  
- Use git as your version control system?
- Use Java?

If you said **'yes'** to at least the last two, then enter the `Samurai Style Cop 2`.  

A checkstyle-based tool that can generate exclusions based on git history so that only the code you stage for commit is checked for styling and quality metrics.  

[All other lines of code are considered death merchants who distribute drugs to our children through schools and on the streets and before they pass through the analyzer their stinking lines will be in garbage bags and ship them back to their maker for fertilizer.](https://www.youtube.com/watch?v=y0IBhUoPlfM)

## Build
Java 16+, Maven 3
```shell
mvn clean package
```

## Usage

### Command line parameters
```shell
Usage: samuraistyle [--checkstyle-stdout] [--use-checkstyle]
                    [--checkstyle-config=CHECKSTYLE_CONFIG_FILE]
                    [--checkstyle-format=<outputFormatType>]
                    [--checkstyle-report=REPORT_FILE] [--limit=<lineLimit>]
                    [-o=FILE] [--static=FILE] (--stdin | -f=DIFF_FILE)
      --checkstyle-config=CHECKSTYLE_CONFIG_FILE
                            Checkstyle configuration file
                              Default: ./sun_checks.xml
      --checkstyle-format=<outputFormatType>
                            Checkstyle output format (XML, SARIF, PLAIN)
                              Default: XML
      --checkstyle-report=REPORT_FILE
                            Checkstyle report (output) file
                              Default: ./checkstyle-corrections.xml
      --checkstyle-stdout   Print checkstyle output to screen
                              Default: false
      --limit=<lineLimit>   Largest line number
                              Default: 50000
  -o, --output=FILE         Suppression file output
                              Default: ./suppression.xml
      --static=FILE         Static suppression file
      --use-checkstyle      Use embedded checkstyle
                              Default: false
Diff Input (Mutually Exclusive Options)  
      -f, --file=DIFF_FILE  Use diff file
      --stdin               Use Standard Input for diff input
```

### Pipe git diff of current branch vs unstaged changes using external CheckStyle
```shell
git --no-pager diff --minimal -U0 -- *.java | java -jar samurai-style-cop-shaded.jar --stdin --output suppressions.xml
```
The above command produces optimal suppressions by generating a minimal diff with 0 lines of context. This may not be the best solution
for all needs, so modify appropriately.

### Pipe git diff of current branch vs staged changes using external CheckStyle
```shell
git --no-pager diff --staged --minimal -U0 -- *.java | java -jar samurai-style-cop-shaded.jar --stdin --output suppressions.xml
```

### Use existing git diff output file
```shell
java -jar samurai-style-cop-shaded.jar --file input.diff --output suppressions.xml
```
Change `input.diff` to name of existing diff file.  
*Note: that this program only accepts `git diff` extended unified diff format.*

For the above examples, you can then use the output file `suppressions.xml` as a suppression filter with [Checkstyle](https://checkstyle.sourceforge.io/config_filters.html#SuppressionFilter) or [Checkstyle Maven Plugin](https://maven.apache.org/plugins/maven-checkstyle-plugin/examples/suppressions-filter.html)

### Invoke Checkstyle
Modify your existing checkstyle configuration xml to use the `SuppressionFilter` using the output name specified in the command line. If you don't specify a name, the output file is named `suppression.xml`
```xml
  <module name="SuppressionFilter">
    <property name="file" value="suppression.xml"/>
    <property name="optional" value="false"/>
  </module>
```
If you don't have a checkstyle configuration, use `sun_checks.xml` or `google_checks.xml` from this repository to start.  
Use external Checkstyle executable or use the embedded checkstyle by enabling it on the command line:
```shell
java -jar samurai-style-cop-shaded.jar --file input.diff  --use-checkstyle --checkstyle-format XML
```
The report output filename defaults to `checkstyle-corrections.xml` regardless of format if a filename is not specified.

### Output Example
`suppression.xml` output example:
```xml
<?xml version="1.0"?><!DOCTYPE suppressions PUBLIC "-//Puppy Crawl//DTD Suppressions 1.0//EN" "http://www.puppycrawl.com/dtds/suppressions_1_0.dtd">
<suppressions>
<suppress checks=".*" files="src[\\/]main[\\/]java[\\/]com[\\/]epicmonstrosity[\\/]samuraistyle[\\/]Main.java" lines="1-4,7-9,12-15,18-27,41-43,46-58,66-68,71-50000"/>
<suppress checks=".*" files="src[\\/]main[\\/]java[\\/]com[\\/]epicmonstrosity[\\/]samuraistyle[\\/]cli[\\/]DiffOptions.java" lines="1-9,12,18-40,46,49-51,54-50000"/>
<suppress checks=".*" files="src[\\/]main[\\/]java[\\/]com[\\/]epicmonstrosity[\\/]samuraistyle[\\/]lint[\\/]CheckstyleSuppressor.java" lines="1-62,65-67,70-74,82-50000"/>
</suppressions>
```

Checkstyle output example (PLAIN format):
```
Starting audit...
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\Main.java:63:48: Parameter diffOptions should be final. [FinalParameters]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:10:5: Missing a Javadoc comment. [JavadocVariable]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:11:20: Variable 'exclusiveInput' must be private and have accessor methods. [VisibilityModifier]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:13:5: Missing a Javadoc comment. [JavadocVariable]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:14:23: Variable 'checkStyleOptions' must be private and have accessor methods. [VisibilityModifier]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:16:5: Missing a Javadoc comment. [JavadocVariable]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:17:10: Variable 'outputFile' must be private and have accessor methods. [VisibilityModifier]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:41:5: Missing a Javadoc comment. [MissingJavadocMethod]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:47:17: Variable 'stdIn' must be private and have accessor methods. [VisibilityModifier]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\cli\DiffOptions.java:52:9: Missing a Javadoc comment. [MissingJavadocMethod]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\lint\CheckstyleSuppressor.java:63:5: Missing a Javadoc comment. [MissingJavadocMethod]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\lint\CheckstyleSuppressor.java:68:5: Missing a Javadoc comment. [MissingJavadocMethod]
[ERROR] .\src\main\java\com\epicmonstrosity\samuraistyle\lint\CheckstyleSuppressor.java:75:5: Missing a Javadoc comment. [MissingJavadocMethod]
Audit done.
```

## Use Cases

#### What can this be used for?  

This tool is mainly designed for very large and old legacy-style code bases where the years past have brought about multiple different styling standards or consists of a lot of cowboy coding.

If your code was passed down to new owners over the years with multiple teams responsible for maintaining it, there's a chance the code style may not be concrete throughout the application. This application can be used to gently integrate a consistent style without overhauling the entire codebase.  

Doing so can not only be a **risk**, but will **muddy** the versioning history. 

#### Here is a short list of possible use cases:
1. A new coding style standard was implemented, yet most of the legacy code doesn't conform and there is too much risk bulk updating it.
2. The codebase is enormous, you don't want to run a linting tool on the entire codebase which can impact performance and time.
3. You want to quality check only the code you modified before pushing.

## Roadmap
- [x] Unified diff with Git extensions reader
- [x] Suppression generator 
- [x] Embedded Checkstyle
- [ ] Support project configuration yaml
- [ ] Multiple checkstyle listeners
---
## What's with the 2?
I wrote a similar tool before that I'm not at liberty to share the code nor do I possess it, so this is a clean room rewrite because I like the name.