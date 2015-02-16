package lingo.demo2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Request;
import org.apache.log4j.*;

import util.HTMLFilter;
/**
 * Example servlet showing LINDO optimization session
 *
 * @author MKA
 */

public class SolveModel extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final ResourceBundle RB = ResourceBundle.getBundle("lingo/demo/LocalStrings",  new Locale("es"));

	private static Logger log = Logger.getLogger(SolveModel.class);	
	
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");
        System.out.println("request.getContextPath() :" + request.getContextPath());
		PropertyConfigurator.configure( this.getClass().getClassLoader().getResource("SolveModel.properties"));
		LindoSolver ls = new LindoSolver();

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");

        String title = RB.getString("solvemodel.title");
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");

        // img stuff not req'd for source code html showing

       // all links relative
        // XXX
        // making these absolute till we work out the
        // addition of a PathInfo issue

        out.println("<a href=\"../reqparams.html\">");
        out.println("<img src=\"../images/code.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"view code\"></a>");
        out.println("<a href=\"../index.html\">");
        out.println("<img src=\"../images/return.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"return\"></a>");

        out.println("<h3>" + title + "</h3>");
        String modelName = request.getParameter("modelname");
        String modelPath = request.getParameter("modelpath");

        out.println(RB.getString("solvemodel.params-in-req") + "<br>");
        if ((modelName != null || modelPath != null) && (modelName.length()!=0)) {
            out.println(RB.getString("solvemodel.modelname"));
            out.println(" = " + HTMLFilter.filter(modelName) + "<br>");
            out.println(RB.getString("solvemodel.modelpath"));
            out.println(" = " + HTMLFilter.filter(modelPath));
			try {			
				log.info("SolveModel is trying to solve "+modelPath+"/"+modelName);
				//ls.solveFile(ls,log,modelPath+"/"+modelName);
				log.debug("SolveModel completed!");
				out.println("<br>SolveModel completed!");
				out.println("<br>Objective :"+HTMLFilter.filter(String.valueOf(ls.dObj[0])));
				out.println("<br>Status :"+HTMLFilter.filter(String.valueOf(ls.nStatus[0])));
				out.println("<br>LINDO API version: " + HTMLFilter.filter(ls.versionId.toString()));
			} catch (Exception ex) {
				System.out.println(ex);
			}					
        } else {
            out.println(RB.getString("solvemodel.no-params"));
			try {			
				log.info("SolveModel is trying to solve built-in model");
				ls.run(ls,log);				
				log.debug("SolveModel completed!");
				out.println("<br>SolveModel completed!");
				out.println("<br>Objective :"+HTMLFilter.filter(String.valueOf(ls.dObj[0])));
				out.println("<br>Status :"+HTMLFilter.filter(String.valueOf(ls.nStatus[0])));
				out.println("<br>LINDO API version: " + HTMLFilter.filter(ls.versionId.toString()));
			} catch (Exception ex) {
				System.out.println(ex);
			}
        }
        out.println("<P>");
        out.print("<form action=\"");
        out.print("SolveModel\" ");
        out.println("method=POST>");
        out.println(RB.getString("solvemodel.modelname"));
        out.println("<input type=text size=20 name=modelname>");
        out.println("<br>");
        out.println(RB.getString("solvemodel.modelpath"));
        out.println("<input type=text size=20 name=modelpath>");
        out.println("<br>");
        out.println("<input type=submit>");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

}


