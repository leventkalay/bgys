import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBirim, Birim } from '../birim.model';

import { BirimService } from './birim.service';

describe('Birim Service', () => {
  let service: BirimService;
  let httpMock: HttpTestingController;
  let elemDefault: IBirim;
  let expectedResult: IBirim | IBirim[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BirimService);
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

    it('should create a Birim', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Birim()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Birim', () => {
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

    it('should partial update a Birim', () => {
      const patchObject = Object.assign({}, new Birim());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Birim', () => {
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

    it('should delete a Birim', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBirimToCollectionIfMissing', () => {
      it('should add a Birim to an empty array', () => {
        const birim: IBirim = { id: 123 };
        expectedResult = service.addBirimToCollectionIfMissing([], birim);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(birim);
      });

      it('should not add a Birim to an array that contains it', () => {
        const birim: IBirim = { id: 123 };
        const birimCollection: IBirim[] = [
          {
            ...birim,
          },
          { id: 456 },
        ];
        expectedResult = service.addBirimToCollectionIfMissing(birimCollection, birim);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Birim to an array that doesn't contain it", () => {
        const birim: IBirim = { id: 123 };
        const birimCollection: IBirim[] = [{ id: 456 }];
        expectedResult = service.addBirimToCollectionIfMissing(birimCollection, birim);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(birim);
      });

      it('should add only unique Birim to an array', () => {
        const birimArray: IBirim[] = [{ id: 123 }, { id: 456 }, { id: 63344 }];
        const birimCollection: IBirim[] = [{ id: 123 }];
        expectedResult = service.addBirimToCollectionIfMissing(birimCollection, ...birimArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const birim: IBirim = { id: 123 };
        const birim2: IBirim = { id: 456 };
        expectedResult = service.addBirimToCollectionIfMissing([], birim, birim2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(birim);
        expect(expectedResult).toContain(birim2);
      });

      it('should accept null and undefined values', () => {
        const birim: IBirim = { id: 123 };
        expectedResult = service.addBirimToCollectionIfMissing([], null, birim, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(birim);
      });

      it('should return initial array if no Birim is added', () => {
        const birimCollection: IBirim[] = [{ id: 123 }];
        expectedResult = service.addBirimToCollectionIfMissing(birimCollection, undefined, null);
        expect(expectedResult).toEqual(birimCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
