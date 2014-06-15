package ic2.utils;

import ic2.items.CL;
import ic2.utils.CONS.Admin.SortTypes;

import java.util.Comparator;
import java.util.List;

import android.util.Log;


public class Comp_CL implements Comparator<CL> {

	List<CL> ai_List;
//	SortType sortType;
//	SortOrder sortOrder;
	CONS.Admin.SortTypes sortType;
	
	
	public Comp_CL(SortTypes sortType) {
		// TODO Auto-generated constructor stub
		
		this.sortType = sortType;
		
	}

	public int compare(CL cl1, CL cl2) {
		// TODO Auto-generated method stub
		int res;
		
		switch(sortType) {
		
		case SortBy_Yomi:
			
			res = _compare_Yomi(cl1, cl2);
			
			break;
			
		case SortBy_CreatedAt:
			
			res = _compare_CreatedAt(cl1, cl2);
			
			break;
			
		default:
			
			res = 1;
		
		}

		return res;

//			switch (sortOrder) {
//			
//			case ASC:
//				
////					res = (int) (b1.getCreated_at() - b2.getCreated_at());
//				res = a1.getFile_name().compareTo(a2.getFile_name());
//				
//				break;
//				
//			case DEC:
//				
////					res = (int) -(a1.getCreated_at() - a2.getCreated_at());
//				res = a2.getFile_name().compareTo(a1.getFile_name());
//				
//				break;
//				
//			default:
//				
//				res = 1;
//				
//				break;
//				
//			}
		
		
	}//public int compare(BM b1, BM b2)

	private int 
	_compare_CreatedAt(CL cl1, CL cl2) {
		// TODO Auto-generated method stub

		long diff = (cl1.getCreated_at() - cl2.getCreated_at());
		
		if (diff > 0) {
			
			return 1;
			
		} else if (diff < 0){//if (diff > 0)
			
			return -1;
			
		} else {//if (diff > 0)
			
			return 0;
			
		}//if (diff > 0)
		
	}//_compare_CreatedAt(CL cl1, CL cl2)

	private int 
	_compare_Yomi(CL cl1, CL cl2) {
		// TODO Auto-generated method stub
		
		if (cl1.getYomi() == null) {
			
			// Log
			Log.d("["
					+ "Methods_ic.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber()
					+ " : "
					+ Thread.currentThread().getStackTrace()[2]
							.getMethodName() + "]",
			"cl1.getYomi => null");
			
			return 1;
			
		} else	if (cl2.getYomi() == null) {
			
			// Log
			Log.d("["
					+ "Methods_ic.java : "
					+ +Thread.currentThread().getStackTrace()[2]
							.getLineNumber()
							+ " : "
							+ Thread.currentThread().getStackTrace()[2]
									.getMethodName() + "]",
					"cl2.getYomi => null");
				
			return 1;
			
		}
		
		return (int)
			(cl1.getYomi().compareToIgnoreCase(cl2.getYomi()));
		
	}//_compare_Yomi(CL cl1, CL cl2)

}//public class BMComparator implements Comparator<BM>
