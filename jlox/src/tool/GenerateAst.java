package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: generateAst <output_directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));
    }

    private static void defineAst(String outputDir, String fileBaseName, List<String> types)
        throws IOException {

        String path = outputDir + "/" + fileBaseName + ".java";
        System.out.println("Writing to: " + path);

        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            writer.println("abstract class " + fileBaseName + " {");
            defineVisitor(writer, fileBaseName, types);

            // The base accept() method.
            writer.println();
            writer.println("  abstract <R> R accept(Visitor<R> visitor);");

            for (String exprType : types) {
                String typeName = exprType.split(":")[0].trim();
                String fields = exprType.split(":")[1].trim();
                defineType(writer, fileBaseName, typeName, fields);
            }

            writer.println("}");
        }
    }

    private static void defineType(PrintWriter writer, String baseName, String exprTypeName, String fields) {
        writer.println("public static class " + exprTypeName + " extends " + baseName + " {");
        // individual fields
        for (String field : fields.split(", ")) {
            writer.println("final " + field + ";");
        }
        // constructor
        writer.println(exprTypeName + "(" + fields + ") {");
        for (String field : fields.split(", ")) {
            String fieldType = field.split(" ")[0];
            String fieldName = field.split(" ")[1];
            writer.println("this." + fieldName + " = " + fieldName + ";");
        }
        writer.println("}");

        // visitor pattern
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" + exprTypeName + baseName + "(this);");
        writer.println("    }");

        // closing brace
        writer.println("}");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("  interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("  }");
    }
}
