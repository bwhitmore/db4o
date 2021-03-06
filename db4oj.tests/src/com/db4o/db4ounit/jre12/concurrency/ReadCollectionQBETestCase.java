/* Copyright (C) 2004 - 2007  Versant Inc.   http://www.db4o.com */

package com.db4o.db4ounit.jre12.concurrency;

import java.util.*;

import com.db4o.*;
import com.db4o.db4ounit.common.persistent.*;
import com.db4o.ext.*;

import db4ounit.*;
import db4ounit.extensions.*;

/**
 */
@decaf.Ignore(decaf.Platform.JDK11)
public class ReadCollectionQBETestCase extends Db4oClientServerTestCase {

	public static void main(String[] args) {
		new ReadCollectionQBETestCase().runConcurrency();
	}
	
	private static String testString = "simple test string";
	
	private static int LIST_SIZE = 100;

	public List list = new ArrayList();

	protected void store() throws Exception {
		for (int i = 0; i < LIST_SIZE; i++) {
			SimpleObject o = new SimpleObject(testString + i, i);
			list.add(o);
		}
		store(list);
	}

	public void concReadCollection(ExtObjectContainer oc) throws Exception {
		ObjectSet result = oc.queryByExample(new ArrayList());
		Assert.areEqual(1, result.size());
		List resultList = (List) result.next();
		Assert.areEqual(list, resultList);
	}
}
