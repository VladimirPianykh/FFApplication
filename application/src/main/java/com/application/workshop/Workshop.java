package com.application.workshop;

public enum Workshop {
	Sawmill("лесопильный цех",
			new WorkArea[] {
					new WorkArea("Лесопильная линия №1"),
					new WorkArea("Лесопильная линия №2")
			}),
	DryComplex("Сушильный комплекс",
			new WorkArea[] {
					new WorkArea("Сушильная камера №1"),
					new WorkArea("Сушильная камера №2"),
					new WorkArea("Сушильная камера №3"),
					new WorkArea("Сушильная камера №4")
			}),
	Processing("Цех строжки и обработки",
			new WorkArea[] {
					new WorkArea("Линия строжки №1"),
					new WorkArea("Линия строжки №2"),
					new WorkArea("Линия строжки №3")
			}),
	Pellet("Пеллетный цех",
			new WorkArea[] {
					new WorkArea("Дробилка"),
					new WorkArea("Сушилка"),
					new WorkArea("Гранулятор №1"),
					new WorkArea("Гранулятор №2")
			});

	public final String toRussian;
	public final WorkArea[] parts;

	Workshop(String toRussian, WorkArea[] workShopParts) {
		this.toRussian = toRussian;
		this.parts = workShopParts;
	}

	@Override
	public String toString() {
		return toRussian;
	}
}
