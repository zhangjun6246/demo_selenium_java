package com.globalegrow.custom;

import org.testng.Assert;

public class test {

	public static void main(String[] args) {
		String successfull = "We appreciate your shopping!Your order has been submitted successfully.";
		if(successfull.contains("Your order has been submitted successfully")){
			System.out.println(1);
		}else{
			System.out.println(2);
		}
	}

}
