package lingo.demo2;

import com.lindo.*;

import org.apache.log4j.Logger;

class ex_userdata {
	int numCback;
	int numBestK;
}

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
		int nM = 3;

		/* Number of variables */
		int nN = 2;

		int nSolStatus[] = new int[1];

		/* >>> Step 1 <<< Read license file and create a LINDO environment. */
		cLicenseKey.setLength(0);
		nErrorCode[0] = LSloadLicenseString(PATH_LINDOAPI_8_0_LICENSE,
				cLicenseKey);
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

		/* >>> Step 2 <<< Create a model in the environment. */
		pModel = LScreateModel(pEnv, nErrorCode);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		/*
		 * >>> Step 3 <<< Specify the model.
		 * 
		 * /* The direction of optimization
		 */
		int nDir = LS_MAX;

		/* The objective's constant term */
		double dObjConst = 0.;

		/* The coefficients of the objective function */
		double adC[] = new double[] { 20., 30. };

		/* The right-hand sides of the constraints */
		double adB[] = new double[] { 120., 60., 50. };

		/* The constraint types */
		String acConTypes = "LLL";

		/* The number of nonzeros in the constraint matrix */
		int nNZ = 4;

		/* The indices of the first nonzero in each column */
		int anBegCol[] = new int[] { 0, 2, nNZ };

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
		double pdLower[] = null, pdUpper[] = null;

		String varnames[] = { "Variable1", "Variable2" };
		String connames[] = { "Constraint1", "Constraint2", "Constraint3" };

		/*
		 * We have now assembled a full description of the model. We pass this
		 * information to LSloadLPData with the following call.
		 */
		nErrorCode[0] = LSloadLPData(pModel, nM, nN, nDir, dObjConst, adC, adB,
				acConTypes, nNZ, anBegCol, pnLenCol, adA, anRowX, pdLower,
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
		double adX[] = new double[2], adR[] = new double[2], adS[] = new double[3], adY[] = new double[3], dObj[] = new double[1];

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
		for (i = 0; i < nN; i++) {
			nErrorCode[0] = LSgetVariableNamej(pModel, i, varName);
			log.debug(varName.toString() + "\t" + adX[i] + "\t" + adR[i]);
			// nErrorCode[0]=LSgetVariableIndex(pModel,varName.toString(),varIdx);
			// log.debug(varName.toString() + "\t" + varIdx[0]);
			varName.setLength(0);
		}
		log.debug("");

		log.debug("Dual solution");
		for (i = 0; i < nM; i++)
			log.debug(connames[i] + "\t" + adY[i] + "\t" + adS[i]);
		System.out.print("\n");
		nErrorCode[0] = LSwriteSolution(pModel, "logs/test/LindoSolver.sol");

		/* >>> Step 6 <<< Delete the LINDO environment */
		nErrorCode[0] = LSdeleteModel(pModel);

		nErrorCode[0] = LSdeleteEnv(pEnv);

	}

	public static void solveFile(LindoSolver ls, Logger log, String szFile)
			throws Exception {
		_log = log;
		int i, neq, nle, nge;
		int m[] = new int[1];
		int n[] = new int[1];
		int nbin[] = new int[1];
		int ngin[] = new int[1];
		int ncont[] = new int[1];
		int nStatus[] = new int[1];
		StringBuffer csense = new StringBuffer();
		int verbose = 1;
		String szOut;

		// Read license file and create a LINDO environment.
		cLicenseKey.setLength(0);
		nErrorCode[0] = LSloadLicenseString(PATH_LINDOAPI_8_0_LICENSE,
				cLicenseKey);
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

		pModel = LScreateModel(pEnv, nErrorCode);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		log.debug("");
		szOut = String.format("Reading %s as MPS file.", szFile);
		log.debug(szOut);
		nErrorCode[0] = LSreadMPSFile(pModel, szFile, 0);
		if (nErrorCode[0] != LSERR_NO_ERROR) {
			log.debug("Failed..");
			szOut = String.format("Reading %s as LINDO formatted file. ",
					szFile);
			log.debug(szOut);
			nErrorCode[0] = LSreadLINDOFile(pModel, szFile);
			if (nErrorCode[0] != LSERR_NO_ERROR) {
				log.debug("Failed..");
				szOut = String.format("Reading %s as MPI file.", szFile);
				log.debug(szOut);
				nErrorCode[0] = LSreadMPIFile(pModel, szFile);
				try {
					APIErrorCheck(pEnv);
				} catch (Exception ex) {
					throw (ex);
				}
			}
		}

		nErrorCode[0] = LSgetInfo(pModel, LS_IINFO_NUM_VARS, n);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		nErrorCode[0] = LSgetInfo(pModel, LS_IINFO_NUM_CONS, m);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		nErrorCode[0] = LSgetLPData(pModel, null, null, null, null, csense,
				null, null, null, null, null, null);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		neq = 0;
		nle = 0;
		nge = 0;
		for (i = 0; i < m[0]; i++) {
			if (csense.charAt(i) == 'E')
				neq++;
			else if (csense.charAt(i) == 'L')
				nle++;
			else
				nge++;
		}

		nErrorCode[0] = LSgetInfo(pModel, LS_IINFO_NUM_CONT, ncont);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		nErrorCode[0] = LSgetInfo(pModel, LS_IINFO_NUM_BIN, nbin);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		nErrorCode[0] = LSgetInfo(pModel, LS_IINFO_NUM_INT, ngin);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		mydata.numCback = 0; // total number of callbacks
		mydata.numBestK = 0; // number of best-k integer solutions (for integer
								// models)

		log.debug("");
		log.debug("");
		log.debug("Model statistics");
		log.debug("\t constraints        = " + m[0]);
		log.debug("\t     + equalities   = " + neq);
		log.debug("\t     + inequalities = " + (nle + nge));
		log.debug("\n");
		log.debug("\t variables          = " + n[0]);
		log.debug("\t     + binary int   = " + nbin[0]);
		log.debug("\t     + general int  = " + ngin[0]);
		log.debug("\t     + continuous   = " + ncont[0]);

		verbose = 2;
		// nErrorCode[0] =
		// LSsetModelIntParameter(pModel,LS_IPARAM_LP_PRELEVEL,0);
		// nErrorCode[0] =
		// LSsetModelIntParameter(pModel,LS_IPARAM_LP_PRINTLEVEL,verbose);
		try {
			APIErrorCheck(pEnv);
		} catch (Exception ex) {
			throw (ex);
		}

		if ((nbin[0] + ngin[0]) == 0) {
			log.debug("");
			log.debug("Optimizing continuous model");
			if (verbose > 1)
				nErrorCode[0] = LSsetModelLogfunc(pModel, "jLogback", ls);
			else if (verbose > 0)
				nErrorCode[0] = LSsetCallback(pModel, "jCallback", ls);
			try {
				APIErrorCheck(pEnv);
			} catch (Exception ex) {
				throw (ex);
			}
			nErrorCode[0] = LSoptimize(pModel, 0, nStatus);
			try {
				APIErrorCheck(pEnv);
			} catch (Exception ex) {
				throw (ex);
			}

		} else {
			log.debug("");
			log.debug("Optimizing integer model");
			if (verbose > 1)
				nErrorCode[0] = LSsetModelLogfunc(pModel, "jLogback", ls);
			else if (verbose > 0) {
				// nErrorCode[0] = LSsetCallback(pModel,"jMIPCallback",ls);
				try {
					APIErrorCheck(pEnv);
				} catch (Exception ex) {
					throw (ex);
				}
				nErrorCode[0] = LSsetMIPCallback(pModel, "jNewMIPCallback", ls);
				try {
					APIErrorCheck(pEnv);
				} catch (Exception ex) {
					throw (ex);
				}
			}
			try {
				APIErrorCheck(pEnv);
			} catch (Exception ex) {
				throw (ex);
			}
			nErrorCode[0] = LSsolveMIP(pModel, nStatus);
			try {
				APIErrorCheck(pEnv);
			} catch (Exception ex) {
				throw (ex);
			}

			log.debug("Searching for K-best integer solutions");
			nErrorCode[0] = LSsetMIPCallback(pModel, null, ls);
			nErrorCode[0] = LSgetKBestMIPSols(pModel,
					"LindoSolver/nextmip.sol", "jNextMIPCallback", ls, 10);
			try {
				APIErrorCheck(pEnv);
			} catch (Exception ex) {
				throw (ex);
			}
		}
		log.debug("Done!");

		szOut = String.format("Status = %d", nStatus[0]);
		log.debug(szOut);
		szOut = String.format("Total callbacks = %d", mydata.numCback);
		log.debug(szOut);

		double adx[] = new double[n[0]];
		double ady[] = new double[m[0]];
		double adDecObj[] = new double[n[0]];
		double adIncObj[] = new double[n[0]];
		double adDecRhs[] = new double[m[0]];
		double adIncRhs[] = new double[m[0]];
		double obj[] = new double[1];

		if ((nbin[0] + ngin[0]) == 0) {
			LSgetInfo(pModel, LS_DINFO_POBJ, obj);
			LSgetPrimalSolution(pModel, adx);
			LSgetDualSolution(pModel, ady);
			if (false) {
				LSgetConstraintRanges(pModel, adDecRhs, adIncRhs);
				LSgetObjectiveRanges(pModel, adDecObj, adIncObj);
				log.debug("\nPrimal Solution (with ranges)\n");
				for (i = 0; i < n[0]; i++)
					log.debug(adx[i] + "\t" + adDecObj[i] + "\t" + adIncObj[i]);

				log.debug("\nDual Solution (with ranges)\n");
				for (i = 0; i < m[0]; i++)
					log.debug(ady[i] + "\t" + adDecRhs[i] + "\t" + adIncRhs[i]);
			}

		} else {
			LSgetInfo(pModel, LS_DINFO_MIP_OBJ, obj);
			LSgetMIPPrimalSolution(pModel, adx);
		}

		nErrorCode[0] = LSdeleteModel(pModel);

		nErrorCode[0] = LSdeleteEnv(pEnv);
	}/* main */

	private static ex_userdata mydata = new ex_userdata();
	private static int iter[] = new int[1];
	private static int n_vars[] = new int[1];
	private static double pobj[] = new double[1];
	private static double bestbound[] = new double[1];
	private static double pinf[] = new double[1];
	private static Object pEnv = null;
	private static Object pModel = null;

	private static void jLogback(Object pMod, String szMessage, Object pls) {
		_log.debug(szMessage.substring(1, szMessage.length()));
	}

	/* A callback function for continuous models */
	private static int jCallback(Object pMod, int nLoc, Object pls) {
		int ncalls = 0;
		LindoSolver nls = null;
		ex_userdata _mydata;
		try {
			nls = (LindoSolver) pls;
			_mydata = (ex_userdata) nls.mydata;

			_mydata.numCback++;
			ncalls = _mydata.numCback;
		} catch (Exception e) {
			_log.debug(e.toString());
		}

		LSgetCallbackInfo(pMod, 0, LS_DINFO_PINFEAS, pinf);
		LSgetCallbackInfo(pMod, 0, LS_DINFO_POBJ, pobj);
		LSgetCallbackInfo(pMod, 0, LS_IINFO_SIM_ITER, iter);
		String szOut = String.format(
				"\n@callback calls=%3d, iter=%8d, pobj = %+.6e,  pinf = %.6e",
				ncalls, iter[0], pobj[0], pinf[0]);
		_log.debug(szOut);
		return 0;
	}

	/* A callback function for integer models */
	private static int jMIPCallback(Object pMod, int nLoc, Object pls) {
		int ncalls = 0;
		LindoSolver nls = null;
		ex_userdata _mydata;
		try {
			nls = (LindoSolver) pls;
			_mydata = (ex_userdata) nls.mydata;

			_mydata.numCback++;
			ncalls = _mydata.numCback;
		} catch (Exception e) {
			_log.debug(e.toString());
		}

		// return if not calling from MIP optimizer
		if (nLoc != LSLOC_MIP)
			return 0;

		LSgetCallbackInfo(pMod, 0, LS_DINFO_MIP_BESTBOUND, bestbound);
		LSgetCallbackInfo(pMod, 0, LS_DINFO_MIP_OBJ, pobj);
		LSgetCallbackInfo(pMod, 0, LS_IINFO_MIP_SIM_ITER, iter);
		String szOut = String
				.format("\n@callback calls=%3d, iter=%8d, pobj = %+.6e,  bestbnd = %.6e",
						ncalls, iter[0], pobj[0], bestbound[0]);
		_log.debug(szOut);
		return 0;
	}

	/*
	 * A callback function to be called at every new integer solution (for
	 * integer models)
	 */
	private static int jNewMIPCallback(Object pMod, Object pls, double obj,
			double x[]) {
		int ncalls = 0;
		LindoSolver nls = null;
		ex_userdata _mydata;
		try {
			nls = (LindoSolver) pls;
			_mydata = (ex_userdata) nls.mydata;

			_mydata.numCback++;
			ncalls = _mydata.numCback;
		} catch (Exception e) {
			_log.debug(e.toString());
		}

		LSgetCallbackInfo(pMod, 0, LS_DINFO_MIP_BESTBOUND, bestbound);
		LSgetCallbackInfo(pMod, 0, LS_DINFO_MIP_OBJ, pobj);
		LSgetCallbackInfo(pMod, 0, LS_IINFO_MIP_SIM_ITER, iter);
		String szOut = String
				.format("\n@new integer solution=%3d, iter=%8d, pobj = %+.6e,  bestbnd = %.6e (*)",
						ncalls, iter[0], pobj[0], bestbound[0]);
		_log.debug(szOut);
		return 0;
	}

	/*
	 * A callback function to be called at next incumbent solution (for global
	 * solver)
	 */
	private static int jNextMIPCallback(Object pMod, Object pls, double obj,
			double x[]) {
		int ncalls = 0;
		int nbestk = 0;
		LindoSolver nls = null;
		ex_userdata _mydata;
		try {
			nls = (LindoSolver) pls;
			_mydata = (ex_userdata) nls.mydata;

			_mydata.numCback++;
			_mydata.numBestK++;
			nbestk = _mydata.numBestK;
		} catch (Exception e) {
			_log.debug(e.toString());
		}

		LSgetCallbackInfo(pMod, 0, LS_DINFO_MIP_BESTBOUND, bestbound);
		LSgetCallbackInfo(pMod, 0, LS_DINFO_MIP_OBJ, pobj);
		LSgetCallbackInfo(pMod, 0, LS_IINFO_MIP_SIM_ITER, iter);
		String szOut = String
				.format("\n@next best integer solution=%3d, iter=%8d, pobj = %+.6e,  bestbnd = %.6e (*)",
						nbestk, iter[0], pobj[0], bestbound[0]);
		_log.debug(szOut);
		return 0;
	}
}
