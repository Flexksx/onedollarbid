class Encoder:
    def __init__(self):
        pass

    def encode(self, obj: any) -> str:
        if isinstance(obj, list):
            return self.__encode_list(obj)
        elif isinstance(obj, dict):
            return self.__encode_dict(obj)
        elif hasattr(obj, "__dict__"):
            return self.__encode_object(obj)
        else:
            return self.__encode_value(obj)

    def __encode_list(self, lst: list) -> str:
        return "[" + ", ".join(self.encode(item) for item in lst) + "]"

    def __encode_dict(self, dct: dict) -> str:
        items = [f"{self.__encode_value(key)}: {self.encode(
            value)}" for key, value in dct.items()]
        return "{" + ", ".join(items) + "}"

    def __encode_object(self, obj: object) -> str:
        return self.__encode_dict(obj.__dict__)

    def __encode_value(self, value: any, log=False) -> str:
        if isinstance(value, str):
            if log:
                print(f'GOT STRING "{value}"')
            return f'"{value}"'
        elif isinstance(value, (int, float)) and not isinstance(value, bool):
            if log:
                print(f'GOT NUMBER "{value}"')
            return str(value)
        elif isinstance(value, bool):
            if log:
                print(f'GOT BOOL "{value}"')
            return str(value).lower()
        elif value is None:
            if log:
                print(f'GOT NONE "{value}"')
            return "null"
        else:
            raise ValueError(f"Unsupported type: {type(value)}")
