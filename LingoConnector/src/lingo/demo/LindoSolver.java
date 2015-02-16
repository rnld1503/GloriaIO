package lingo.demo;

import com.lindo.*;

import org.apache.log4j.Logger;

public class LindoSolver extends Lindo {
	
	private static final String PATH_LINDOAPI_8_0_LICENSE = "C:/Lindoapi/license/lndapi90.lic";
	private static Logger _log = null;
	private static int nErrorCode[] = new int[1];
	private static StringBuffer cErrorMessage = new StringBuffer( LS_MAX_ERROR_MESSAGE_LENGTH);
	private static StringBuffer cLicenseKey = new StringBuffer( LS_MAX_ERROR_MESSAGE_LENGTH);
	
	public static StringBuffer versionId = new StringBuffer( LS_MAX_ERROR_MESSAGE_LENGTH);
	
	// RL:
//	public static double adX[] = new double[36];
//	public static double adR[] = new double[36];
//	public static double adS[] = new double[38];
//	public static double adY[] = new double[38];
//	
	public static double adX[] = new double[20];
	public static double adR[] = new double[20];
	public static double adS[] = new double[20];
	public static double adY[] = new double[20];
	
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
		int numeroDeRestricciones = 38;

/* Number of variables */
		int numeroDeVariables = 36;

		
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
		int nDir = LS_MIN;

		/* The objective's constant term */
		double dObjConst = 0.;

/* The coefficients of the objective function */
		/* Max 20X1 + 30X2  */
		double coefFunionObjetivo[] = new double[] { 70.8, 72.6, 70.6, 68.8,
				68.9, 69.3, 62.9, 62.5, 63.3, 65.5, 65.7, 67.2, 4.8, 4.8, 4.8,
				4.8, 4.8, 4.8, 4.8, 4.8, 4.8, 4.8, 4.8, 4.8, 25.92, 25.92,
				25.92, 25.92, 25.92, 25.92, 18.24, 18.24, 18.24, 18.24, 18.24,
				18.24 };

		/* The right-hand sides of the constraints */
		/* equivalencias de las restricciones */
		double equivalenciasRestricciones[] = new double[] { 721380, 643337,
				679645, 602704, 780689, 692310, 839340, 744320, 842391, 747026,
				834412, 739950, 10000, 9000, 6120, 5472, 5976, 5904, 5976,
				5904, 44640, 40320, 44640, 43200, 44640, 43200, 10000, 10000,
				10000, 10000, 10000, 10000, 36000, 0, 0, 0, 0, 0 };

		System.out.println("equivalenciasRestricciones :" + equivalenciasRestricciones.length);
		/* The constraint types ???????  */
		/* 
		 * L: less than
		 * E: equal to
		 * G: great than
		 * N: neutrals
		 * */
		String acConTypes = "EEEEEEEEEEEEGGLLLLLLLLLLLLLLLLLLEEEEEE";

		System.out.println("acConTypes:" + acConTypes.length());
		/* The number of nonzeros in the constraint matrix */
		/*Numero de nonzeros en la matrix de las restricciones*/
		int numeroNonZero = 106;

		/* The indices of the first nonzero in each column */
		/*indice del primer nonZero en cada columna*/
		/*
		 * 
		 * 
		 */
		
		
		
		/* The nonzero coefficients de las restricciones */
		double adA[] = new double[] { 
				1,-1,
				1,-1,
				1,1,-1,
				1,1,1,
				1,1,-1,
				1,1,-1,
				1,1,-1,
				1,1,-1,
				1,1,-1,
				1,1,-1,
				1,1,-1,
				1,1,-1,
				1,
				1,
				(1/562),225,
				(1/562),225,
				(1/562),225,
				(1/562),225,
				(1/562),225,
				(1/562),225,
				(1/48), (1/50),
				(1/48), (1/50),
				(1/48), (1/50),
				(1/48), (1/50),
				(1/48), (1/50),
				(1/48),(1/50),
				0.162,0.162,
				0.162,0.162,
				0.162,0.162,
				0.162,0.162,
				0.162,0.162,
				0.162,0.162,
				1,1,-1,1,
				-1,1,-1,1,-1,1,
				-1,1,-1,1,-1,1,
				-1,1,-1,1,-1,1,
				-1,1,-1,1,-1,1,
				-1,1,-1,1,-1,1
				};
		

		
		
		System.out.println("adA :" + adA.length);
		
		int anBegCol[] = new int[] { 0,5,10,15,20,25,29,34,39,44,49,54,58,61,64,67,70,73,76,79,82,85,88,91,94,95,96,97,98,99,100,101,102,103,104,105, numeroNonZero };
		

		System.out.println("anBegCol :" + anBegCol.length);
		/* The row indices of the nonzero coefficients */

		
		int anRowX[] = new int[] { 0,14,20,32,33,2,15,21,33,34,4,16,22,34,35,6,17,23,35,36,8,18,24,36,37,10,19,25,37,1,14,20,32,33,3,15,21,33,34,5,16,22,34,35,7,17,23,35,36,9,18,24,36,37,11,19,25,37,0,2,26,2,4,27,4,6,28,6,8,29,8,10,30,10,12,31,1,3,26,3,5,27,5,7,28,7,9,29,9,11,30,11,13,31,32,33,34,35,36,37,32,33,34,35,36,37 };
		
		System.out.println("anRowX :" + anRowX.length);
		/*
		 * The length of each column. Since we aren't leaving any blanks in our
		 * matrix, we can set this to NULL
		 */
		int pnLenCol[] = null;


		/*
		 * Simple upper and lower bounds on the variables. By default, all
		 * variables have a lower bound of zero and an upper bound of infinity.
		 * Therefore pass NULL pointers in order to use these default values.
		 */
		double pdLower[] = null;
		double pdUpper[] = null;

		String varnames[] = { "X11","X12","X13","X14","X15","X16","X21","X22","X23","X24","X25","X26","S11","S12","S13","S14","S15","S16","S21","S22","S23","S24","S25","S26","i1","i2","i3","i4","i5","i6","d1","d2","d3","d4","d5","d6" };
		String connames[] = { "R1","R2","R3","R4","R5","R6","R7","R8","R9","R10","R11","R12","R13","R14","R15","R16","R17","R18","R19","R20","R21","R22","R23","R24","R25","R26","R27","R28","R29","R30","R31","R32","R33","R34","R35","R36","R37","R38" };

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
		double adX[] = new double[20];
		double adR[] = new double[20];
		double adS[] = new double[20];
		double adY[] = new double[20];
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
		//nErrorCode[0] = LSwriteSolution(pModel, "logs/test/LindoSolver.sol");

		/* >>> Step 6 <<< Delete the LINDO environment */
		nErrorCode[0] = LSdeleteModel(pModel);

		nErrorCode[0] = LSdeleteEnv(pEnv);
		
		
		
		

	}

}
