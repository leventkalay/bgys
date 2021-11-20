import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonel, Personel } from '../personel.model';

import { PersonelService } from './personel.service';

describe('Personel Service', () => {
  let service: PersonelService;
  let httpMock: HttpTestingController;
  let elemDefault: IPersonel;
  let expectedResult: IPersonel | IPersonel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonelService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
      soyadi: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Personel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Personel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Personel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          soyadi: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Personel', () => {
      const patchObject = Object.assign(
        {
          adi: 'BBBBBB',
        },
        new Personel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Personel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          soyadi: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Personel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPersonelToCollectionIfMissing', () => {
      it('should add a Personel to an empty array', () => {
        const personel: IPersonel = { id: 123 };
        expectedResult = service.addPersonelToCollectionIfMissing([], personel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personel);
      });

      it('should not add a Personel to an array that contains it', () => {
        const personel: IPersonel = { id: 123 };
        const personelCollection: IPersonel[] = [
          {
            ...personel,
          },
          { id: 456 },
        ];
        expectedResult = service.addPersonelToCollectionIfMissing(personelCollection, personel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Personel to an array that doesn't contain it", () => {
        const personel: IPersonel = { id: 123 };
        const personelCollection: IPersonel[] = [{ id: 456 }];
        expectedResult = service.addPersonelToCollectionIfMissing(personelCollection, personel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personel);
      });

      it('should add only unique Personel to an array', () => {
        const personelArray: IPersonel[] = [{ id: 123 }, { id: 456 }, { id: 86046 }];
        const personelCollection: IPersonel[] = [{ id: 123 }];
        expectedResult = service.addPersonelToCollectionIfMissing(personelCollection, ...personelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personel: IPersonel = { id: 123 };
        const personel2: IPersonel = { id: 456 };
        expectedResult = service.addPersonelToCollectionIfMissing([], personel, personel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personel);
        expect(expectedResult).toContain(personel2);
      });

      it('should accept null and undefined values', () => {
        const personel: IPersonel = { id: 123 };
        expectedResult = service.addPersonelToCollectionIfMissing([], null, personel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personel);
      });

      it('should return initial array if no Personel is added', () => {
        const personelCollection: IPersonel[] = [{ id: 123 }];
        expectedResult = service.addPersonelToCollectionIfMissing(personelCollection, undefined, null);
        expect(expectedResult).toEqual(personelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
