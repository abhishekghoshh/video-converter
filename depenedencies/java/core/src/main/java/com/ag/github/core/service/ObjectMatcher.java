package com.ag.github.core.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ObjectMatcher {
	private static final String BLANK_SPACE = "";
	private static final String VALUES = "values";
	private static final String SPECAIL_CHAR_STAR = "*";

	public boolean check(Object expected, Object actual) throws JsonMappingException, JsonProcessingException {
		if (ifBothNonEmpty(expected, actual)) {
			ObjectMapper mapper = new ObjectMapper();
			Object expectedResponse = mapper.readValue(mapper.writeValueAsString(expected), Object.class);
			Object actualResponse = mapper.readValue(mapper.writeValueAsString(actual), Object.class);
			return checkForClass(expectedResponse, actualResponse);
		} else if (ifBothEmpty(expected, actual)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkForClass(Object expectedValue, Object actualValue) {
		Class<?> classOfExpectedValue = expectedValue.getClass();
		if (classOfExpectedValue.isAssignableFrom(actualValue.getClass())) {
			if (Map.class.isAssignableFrom(classOfExpectedValue)) {
				return checkMap(Map.class.cast(expectedValue), Map.class.cast(actualValue));
			} else if (List.class.isAssignableFrom(classOfExpectedValue)) {
				return checkList(List.class.cast(expectedValue), List.class.cast(actualValue));
			} else {
				return checkObject(expectedValue, actualValue);
			}
		} else {
			return false;
		}
	}

	private boolean checkObject(Object expectedResponse, Object actualResponse) {
		if (ifBothNonEmpty(expectedResponse, actualResponse)) {
			return checkObjectByValue(expectedResponse, actualResponse);
		} else if (ifBothEmpty(expectedResponse, actualResponse)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkObjectByValue(Object expectedResponse, Object actualResponse) {
		if (String.class.isAssignableFrom(expectedResponse.getClass())
				&& String.class.isAssignableFrom(actualResponse.getClass())) {
			String token = String.class.cast(expectedResponse).trim();
			if (token.equalsIgnoreCase(SPECAIL_CHAR_STAR)) {
				return true;
			}
			if (token.contains("*?")) {
				String[] tokens = token.split("\\*\\?");
				String actualString = String.class.cast(actualResponse);
				actualString = isNotEmpty(actualString) ? actualString.trim() : BLANK_SPACE;
				for (String individualToken : tokens) {
					if (isNotEmpty(individualToken) && !actualString.contains(individualToken.trim())) {
						return false;
					}
				}
				return true;
			}
		}
		return expectedResponse.equals(actualResponse);
	}

	private boolean isNotEmpty(String actualString) {
		return null != actualString && !BLANK_SPACE.equals(actualString.trim());
	}

	@SuppressWarnings({ "rawtypes" })
	private boolean checkList(List expectedResponse, List actualResponse) {
		if (isSpecialList(expectedResponse)) {
			return true;
		}
		if (ifBothNonEmpty(expectedResponse, actualResponse)) {
			return checkListByValue(expectedResponse, actualResponse);
		} else if (ifBothEmpty(expectedResponse, actualResponse)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private boolean isSpecialList(List expectedResponse) {
		return !CollectionUtils.isEmpty(expectedResponse) && expectedResponse.size() == 1
				&& String.class.isAssignableFrom(expectedResponse.get(0).getClass())
				&& SPECAIL_CHAR_STAR.equalsIgnoreCase(((String) expectedResponse.get(0)).trim());
	}

	@SuppressWarnings({ "rawtypes" })
	private boolean checkListByValue(List expectedResponse, List actualResponse) {
		if (expectedResponse.size() <= actualResponse.size()) {
			for (int index = 0; index < expectedResponse.size(); index++) {
				Object expectedValue = expectedResponse.get(index);
				Object actualValue = actualResponse.get(index);
				if (!checkForOneItem(expectedValue, actualValue)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean checkForOneItem(Object expectedValue, Object actualValue) {
		if (ifBothNonEmpty(expectedValue, actualValue)) {
			return checkForClass(expectedValue, actualValue);
		} else if (ifBothEmpty(expectedValue, actualValue)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private boolean checkMap(Map expectedResponse, Map actualResponse) {
		if (isSpecialMap(expectedResponse)) {
			return true;
		}
		if (ifBothNonEmpty(expectedResponse, actualResponse)) {
			return checkMapByKey(expectedResponse, actualResponse);
		} else if (ifBothEmpty(expectedResponse, actualResponse)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private boolean isSpecialMap(Map expectedResponse) {
		return !CollectionUtils.isEmpty(expectedResponse) && expectedResponse.containsKey(VALUES)
				&& expectedResponse.entrySet().size() == 1
				&& String.class.isAssignableFrom(expectedResponse.get(VALUES).getClass())
				&& SPECAIL_CHAR_STAR.equalsIgnoreCase(((String) expectedResponse.get(VALUES)).trim());

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean checkMapByKey(Map expectedResponse, Map actualResponse) {
		Set<String> expectedKeys = expectedResponse.keySet();
		for (String key : expectedKeys) {
			Object expectedValue = expectedResponse.get(key);
			Object actualValue = actualResponse.get(key);
			if (!checkForOneItem(expectedValue, actualValue)) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	private boolean ifBothNonEmpty(Map expectedResponse, Map actualResponse) {
		return !CollectionUtils.isEmpty(expectedResponse) && !CollectionUtils.isEmpty(actualResponse);
	}

	@SuppressWarnings("rawtypes")
	private boolean ifBothEmpty(Map expectedResponse, Map actualResponse) {
		return CollectionUtils.isEmpty(expectedResponse) && CollectionUtils.isEmpty(actualResponse);
	}

	@SuppressWarnings("rawtypes")
	private boolean ifBothNonEmpty(List expectedResponse, List actualResponse) {
		return !CollectionUtils.isEmpty(expectedResponse) && !CollectionUtils.isEmpty(actualResponse);
	}

	@SuppressWarnings("rawtypes")
	private boolean ifBothEmpty(List expectedResponse, List actualResponse) {
		return CollectionUtils.isEmpty(expectedResponse) && CollectionUtils.isEmpty(actualResponse);
	}

	private boolean ifBothNonEmpty(Object expectedResponse, Object actualResponse) {
		return null != expectedResponse && null != actualResponse;
	}

	private boolean ifBothEmpty(Object expectedResponse, Object actualResponse) {
		return null == expectedResponse && null == actualResponse;
	}
}
