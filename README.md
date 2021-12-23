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
- [ ] Embedded Checkstyle

## What's with the 2?
I wrote a similar tool before that I'm not at liberty to share the code nor do I possess it, so this is a clean room rewrite because I like the name.