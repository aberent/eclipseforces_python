/*   
   Copyright 2022 Anthony Berent

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package io.github.aberent.eclipseforces.python;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import net.fmoraes.eclipseforces.Contest;
import net.fmoraes.eclipseforces.EclipseForcesPlugin;
import net.fmoraes.eclipseforces.ProblemStatement;
import net.fmoraes.eclipseforces.ProblemStatementView;
import net.fmoraes.eclipseforces.languages.CodeGenerator;
import net.fmoraes.eclipseforces.languages.LanguageSupport;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class PythonLanguageSupport extends LanguageSupport {

	@Override
	protected CodeGenerator createCodeGenerator(List<ProblemStatement> tasks) {
		return new PythonCodeGenerator(tasks);
	}

	@Override
	protected IFile createLanguageProject(IProject project, ProblemStatement problem, Contest contest)
			throws Exception {
		IEclipsePreferences prefs = EclipseForcesPlugin.getProjectPrefs(project);
		prefs.put(ProblemStatementView.CONTEST_KEY, contest.name);
		List<IFile> testFiles = new ArrayList<IFile>();
		for (ProblemStatement ps : getProblems()) {
			prefs.put(ps.getClassName() + ".py",
					String.format("http://www.codeforces.com/contest/%s/problem/%s", contest.id, ps.getID()));
			prefs.put(ps.getClassName() + "_test.py",
					String.format("http://www.codeforces.com/contest/%s/problem/%s", contest.id, ps.getID()));
			prefs.put("timeout." + ps.getClassName() + "_test.py", ps.getTimeLimit());
			prefs.flush();
			IFile solutionFile = project.getFile(ps.getClassName() + ".py");
			solutionFile.create(new ByteArrayInputStream(PythonCodeGenerator.DEFAULT_CODE_TEMPLATE.getBytes()), false,
					null);
			IFile testFile = project.getFile(ps.getClassName() + "_test.py");
			testFile.create(new ByteArrayInputStream(getCodeGenerator().getTestsSource(ps).getBytes()), false, null);
			testFiles.add(testFile);
		}
		return testFiles.get(0);
	}

	@Override
	protected String getCodeEditorID() {
		return "org.python.pydev.editor.PythonEditor";
	}

	@Override
	protected String getCodeTemplate() {
		// TODO Auto-generated method stub
		return "pass";
	}

	@Override
	public String getLanguageName() {
		return "Python";
	}

	@Override
	public String getPerspectiveID() {
		return "org.python.pydev.ui.PythonPerspective";
	}

	@Override
	protected String getSolutionFileName(ProblemStatement ps) {
		return ps.getClassName() + ".py";
	}

}
