package lingo.demo;

import com.lindo.*;

import org.apache.log4j.Logger;

/*
 * 

		Max = 20 * A + 30 * C
		S.T.
			       A + 2 * C <= 120 
			       A         <= 60 
			               C <= 50
		 _  _
		|1  2|
		|1  0|
		|0  1|
		 _  _
	                        _	_ 
		 lineal non ceros: [1 1 2 1]
		            indices 0 1 2 3 
		
		column start [0 2 4]  - el 4 es el tamaño vector lineal non zeros
		
		el indice de los nonZero en la matriz inicial
		`[0 1   0 2]
		
		Solving such a problem with the LINDO API involves
		the following steps:
   		
   		1. Create a LINDO environment.
   		2. Create a model in the environment.
   		3. Specify the model.
   		4. Perform the optimization.
   		5. Retrieve the solution.
 */

public class LindoSolver extends Lindo {
	
	private static final String PATH_LINDOAPI_8_0_LICENSE = "C:/Lindoapi/license/lndapi90.lic";
	private static Logger _log = null;
	private static int nErrorCode[] = new int[1];
	private static StringBuffer cErrorMessage = new StringBuffer( LS_MAX_ERROR_MESSAGE_LENGTH);
	private static StringBuffer cLicenseKey = new StringBuffer( LS_MAX_ERROR_MESSAGE_LENGTH);
	
	public static StringBuffer versionId = new StringBuffer( LS_MAX_ERROR_MESSAGE_LENGTH);
	
	// RL:
	public static double adX[] = new double[2];
	public static double adR[] = new double[2];
	public static double adS[] = new double[3];
	public static double adY[] = new double[3];
	public static double dObj[] = new double[1];
	public static int nStatus[] = new int[1];
	
	private static Object pEnv = null;
	private static Object pModel = null;

	// Generalized error Reporting function
	private static void APIErrorCheck(Object /* long */pEnv) throws Exception {
		
		if (0 != nErrorCode[0]) {
			cErrorMessage.setLength(0);
			LSgetErrorMessage(pEnv, nErrorCode[0], cErrorMessage);
			_log.debug("\nError " + nErrorCode[0] + ": " + cErrorMessage);
			_log.debug("");
			throw (new Exception("Error " + nErrorCode[0] + ": "
					+ cErrorMessage));
		}
	}

	// Version Reporting function
	private static void APIVERSION() {
		StringBuffer szVersion = new StringBuffer(255);
		StringBuffer szBuild = new StringBuffer(255);
		versionId.setLength(0);
		LSgetVersionInfo(versionId, szBuild);
		_log.debug("\nLINDO API Version " + szVersion.toString() + " built on "
				+ szBuild.toString());
		_log.debug("");
	}

	static {
		// The runtime system executes a class's static
		// initializer when it loads the class.
		System.loadLibrary("lindojni");
	}

	public static void run(LindoSolver ls, Logger log) throws Exception {
		_log = log;
		
/* Number of constraints */
		int numeroDeRestricciones = 3;

/* Number of variables */
		int numeroDeVariables = 2;

		
		int nSolStatus[] = new int[1];

		/* >>> paso 1 <<< leer el archivo de licencia  y crear el entorno Lingo. */
		cLicenseKey.setLength(0);
		nErrorCode[0] = LSloadLicenseString(PATH_LINDOAPI_8_0_LICENSE, cLicenseKey);
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		APIVERSION();
		
		
		pEnv = LScreateEnv(nErrorCode, cLicenseKey.toString());
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}
		
		/*===================================================*/
		/* >>> Step 2 <<< Create a model in the environment. */
		/*===================================================*/
		pModel = LScreateModel(pEnv, nErrorCode);
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}
		
		/*
		 * >>> Step 3 <<< Specify the model.
		 * 
		 * /* direccion de la optimizacion (maximizar o minimizar)
		 */
		int nDir = LS_MAX;

		/* The objective's constant term */
		double dObjConst = 0.;

/* The coefficients of the objective function */
		/* Max 20X1 + 30X2  */
		double coefFunionObjetivo[] = new double[] { 20., 30. };

		/* The right-hand sides of the constraints */
		/* equivalencias de las restricciones */
		double equivalenciasRestricciones[] = new double[] { 120., 60., 50. };

		/* The constraint types ???????  */
		/* 
		 * L: less than
		 * E: equal to
		 * G: great than
		 * N: neutral
		 * */
		String acConTypes = "LLL";

		/* The number of nonzeros in the constraint matrix */
		/*Numero de nonzeros en la matrix de las restricciones*/
		int numeroNonZero = 4;

		/* The indices of the first nonzero in each column */
		/*indice del primer nonZero en cada columna*/
		/*
		 * 
		 * 
		 */
		
		int anBegCol[] = new int[] { 0, 2, numeroNonZero };

		/*
		 * The length of each column. Since we aren't leaving any blanks in our
		 * matrix, we can set this to NULL
		 */
		int pnLenCol[] = null;

		/* The nonzero coefficients */
		double adA[] = new double[] { 1., 1., 2., 1. };

		/* The row indices of the nonzero coefficients */
		
		int anRowX[] = new int[] { 0, 1, 0, 2 };

		/*
		 * Simple upper and lower bounds on the variables. By default, all
		 * variables have a lower bound of zero and an upper bound of infinity.
		 * Therefore pass NULL pointers in order to use these default values.
		 */
		double pdLower[] = null;
		double pdUpper[] = null;

		String varnames[] = { "Variable1", "Variable2" };
		String connames[] = { "Constraint1", "Constraint2", "Constraint3" };

		/*
		 * We have now assembled a full description of the model. We pass this
		 * information to LSloadLPData with the following call.
		 */
		nErrorCode[0] = LSloadLPData(pModel, numeroDeRestricciones, numeroDeVariables, nDir, dObjConst, coefFunionObjetivo, equivalenciasRestricciones,
				acConTypes, numeroNonZero, anBegCol, pnLenCol, adA, anRowX, pdLower,
				pdUpper);
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		nErrorCode[0] = LSloadNameData(pModel, "MyTitle", "MyObj", null, null,
				null, connames, varnames, null);
		
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}
		
		log.debug("LSloadNameData success");

		/* >>> Step 4 <<< Perform the optimization */
		nErrorCode[0] = LSoptimize(pModel, LS_METHOD_PSIMPLEX, nSolStatus);
		
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}
		log.debug("LSoptimize success");

		/* >>> Step 5 <<< Retrieve the solution */
		
		int i;
		double adX[] = new double[2];
		double adR[] = new double[2];
		double adS[] = new double[3];
		double adY[] = new double[3];
		double dObj[] = new double[1];
		

		/* Get the value of the objective */
		nErrorCode[0] = LSgetInfo(pModel, LS_DINFO_POBJ, dObj);
		
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		log.debug("Objective Value = " + dObj[0] + "\n");

		/* Get the variable values */
		nErrorCode[0] = LSgetPrimalSolution(pModel, adX);
		
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		/* Get the slack values */
		nErrorCode[0] = LSgetSlacks(pModel, adS);
		
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		
		/* Get the variable values */
		nErrorCode[0] = LSgetDualSolution(pModel, adY);
		
		
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		
		/* Get the slack values */
		nErrorCode[0] = LSgetReducedCosts(pModel, adR);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		StringBuffer varName = new StringBuffer(255);
		int varIdx[] = new int[1];
		System.out.print("\n");
		log.debug("Primal solution");
		
		for (i = 0; i < numeroDeVariables; i++) {
			nErrorCode[0] = LSgetVariableNamej(pModel, i, varName);
			log.debug(varName.toString() + "\t" + adX[i] + "\t" + adR[i]);
			// nErrorCode[0]=LSgetVariableIndex(pModel,varName.toString(),varIdx);
			// log.debug(varName.toString() + "\t" + varIdx[0]);
			varName.setLength(0);
		}
		log.debug("");

		log.debug("Dual solution");
		for (i = 0; i < numeroDeRestricciones; i++)
			log.debug(connames[i] + "\t" + adY[i] + "\t" + adS[i]);
		System.out.print("\n");
		nErrorCode[0] = LSwriteSolution(pModel, "logs/test/LindoSolver.sol");

		/* >>> Step 6 <<< Delete the LINDO environment */
		nErrorCode[0] = LSdeleteModel(pModel);

		nErrorCode[0] = LSdeleteEnv(pEnv);

	}

}
