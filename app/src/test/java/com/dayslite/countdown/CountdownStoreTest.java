package com.dayslite.countdown;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class CountdownStoreTest {

    @Test
    public void parseEventsSkipsOnlyTheCorruptedEntry() {
        String raw = "[" +
                "{\"id\":\"a\",\"title\":\"Valid One\",\"targetDate\":\"2026-01-01\",\"note\":\"\",\"color\":\"#2563EB\",\"repeatYearly\":false,\"createdAt\":1,\"updatedAt\":1}," +
                "{\"id\":\"b\",\"title\":\"Missing Date\"}," +
                "{\"id\":\"c\",\"title\":\"Valid Two\",\"targetDate\":\"2026-02-02\",\"note\":\"\",\"color\":\"#10B981\",\"repeatYearly\":false,\"createdAt\":2,\"updatedAt\":2}" +
                "]";

        List<CountdownEvent> events = CountdownStore.parseEvents(raw);

        assertEquals(2, events.size());
        assertEquals("a", events.get(0).id);
        assertEquals("c", events.get(1).id);
    }
}
