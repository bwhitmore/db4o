/* Copyright (C) 2009  Versant Inc.   http://www.db4o.com */
package com.db4o.db4ounit.common.cs.config;

import java.util.*;

import com.db4o.cs.*;
import com.db4o.cs.config.*;
import com.db4o.cs.internal.config.*;
import com.db4o.ext.*;

import db4ounit.*;
import db4ounit.extensions.dbmock.*;

public class ClientConfigurationItemUnitTestCase implements TestLifeCycle {

	private List<DummyConfigurationItem> _applied;
	private ClientConfigurationImpl _config;
	
	public void testPrepareApply() {
		List<DummyConfigurationItem> items = Arrays.asList(
				new DummyConfigurationItem(_applied),
				new DummyConfigurationItem(_applied)
		);
		for (DummyConfigurationItem item : items) {
			_config.addConfigurationItem(item);
			Assert.areEqual(1, item.prepareCount());
		}
		Assert.areEqual(0, _applied.size());
		_config.applyConfigurationItems(new MockClient());
		assertListsAreEqual(items, _applied);
		for (DummyConfigurationItem item : items) {
			Assert.areEqual(1, item.prepareCount());
		}
	}
	
	public void testAddTwice() {
		DummyConfigurationItem item = new DummyConfigurationItem(_applied);
		_config.addConfigurationItem(item);
		_config.addConfigurationItem(item);
		_config.applyConfigurationItems(new MockClient());
		Assert.areEqual(1, item.prepareCount());
		assertListsAreEqual(Arrays.asList(item), _applied);
	}

	private <T> void assertListsAreEqual(List<T> a, List<T> b) {
		Assert.areEqual(a.size(), b.size());
		for(int i = 0; i < a.size(); i++) {
			Assert.areEqual(a.get(i), b.get(i));
		}
	}

	public void setUp() throws Exception {
		_applied = new ArrayList<DummyConfigurationItem>();
		_config = (ClientConfigurationImpl) Db4oClientServer.newClientConfiguration();
	}

	public void tearDown() throws Exception {
	}

	private static class DummyConfigurationItem implements ClientConfigurationItem {
		private int _prepareCount = 0;
		private List<DummyConfigurationItem> _applied;
		
		public DummyConfigurationItem(List<DummyConfigurationItem> applied) {
			_applied = applied;
		}
		
		public void apply(ExtClient client) {
			_applied.add(this);
		}

		public void prepare(ClientConfiguration configuration) {
			_prepareCount++;
		}
		
		public int prepareCount() {
			return _prepareCount;
		}
	}
}
