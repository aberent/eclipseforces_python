package io.github.aberent.eclipseforces.python;

import java.util.List;

import net.fmoraes.eclipseforces.ProblemStatement;
import net.fmoraes.eclipseforces.languages.CodeGenerator;

public class PythonCodeGenerator extends CodeGenerator {
	
	public static final String DEFAULT_CODE_TEMPLATE = "def main():\n"
			+ "    pass\n"
			+ "\n"
			+ "\n"
			+ "if __name__ == \"__main__\":\n"
			+ "    main()\n";
	
	public PythonCodeGenerator(List<ProblemStatement> problems) {
		super(problems);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTestsSource(ProblemStatement ps) {
		StringBuilder testFileBuilder = new StringBuilder();
		testFileBuilder.append(
				"import sys\n"
				+ "from unittest import TestCase\n"
				+ "from io import StringIO\n"
				+ "from contextlib import redirect_stdout\n"
				+ "from ");
		String className = ps.getClassName();
		testFileBuilder.append(className);
		testFileBuilder.append(" import main\n"
				+ "\n"
				+ "\n"
				+ "class Test(TestCase):\n");
	    int i = -1;
	    int timeLimit = Integer.parseInt(ps.getTimeLimit());
		for (ProblemStatement.TestCase testCase: ps.getTestCases() ) {
			i++;
			getTestCaseSource(testFileBuilder, className, timeLimit, i, testCase.getInput(), testCase.getOutput());
		}
		return testFileBuilder.toString();
	}

	@Override
	public void getTestCaseSource(StringBuilder result, String className, int timeLimit, int number, String input,
			String output) {
				if (input.charAt(0) == '\n') {
					input = input.substring(1);
				}
				if (output.charAt(0) == '\n') {
					output= output.substring(1);
				}
				result.append("    def test_");
				result.append(number);
				result.append("(self):\n");
				result.append("        sys.stdin = StringIO('''");
				result.append(input);
				result.append("'''\n)\n");
				result.append("        with redirect_stdout(StringIO()) as out:\n");
				result.append("            main()\n");
				result.append("        sys.stdin = sys.__stdin__\n");
				result.append("        self.assertEqual('''");
				result.append(output);
				result.append("''',out.getvalue())\n\n");
	}

}
